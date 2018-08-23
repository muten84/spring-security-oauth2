/**
 * 
 */
package it.eng.areas.ems.mobileagent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.cli.Options;
import org.knowm.sundial.SundialJobScheduler;
import org.pmw.tinylog.Logger;
import org.quartz.core.SchedulerFactory;

import dagger.Component;
import dagger.Lazy;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.job.CheckDownloadedArtifacts;
import it.eng.areas.ems.mobileagent.artifacts.job.JobWrapper;
import it.eng.areas.ems.mobileagent.artifacts.job.MultipleArtifactsJob;
import it.eng.areas.ems.mobileagent.artifacts.main.CLIMain;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.artifacts.util.CommandRunner;
import it.eng.areas.ems.mobileagent.artifacts.util.DiscoveryUtil;
import it.eng.areas.ems.mobileagent.device.DeviceFactory;
import it.eng.areas.ems.mobileagent.http.AgentRestService;
import it.eng.areas.ems.mobileagent.http.WsHandler;
import it.eng.areas.ems.mobileagent.module.AgentModule;

/**
 * @author Bifulco Luigi
 *
 */
class Agent {

	private final Lazy<AgentRestService> agentRestService;

	private final String agentServerPort;

	private final String agentRootFolder;

	private Lazy<CheckDownloadedArtifacts> checkArtifacts;

	private Lazy<MultipleArtifactsJob> checkMultiArtifacts;

	private ArtifactsManagerStateService stateService;

	private JsonStoreService jsonStoreService;

	private ArtifactsManager artifactsManager;

	@Inject
	Agent(Lazy<AgentRestService> agentRestService, @Named("agent.server.rootFolder") String externalStaticFileLocation,
			@Named("agent.server.port") String port, ArtifactsManagerStateService stateService,
			Lazy<CheckDownloadedArtifacts> checkArtifacts, Lazy<MultipleArtifactsJob> checkMultiArtifacts,
			JsonStoreService jsonStoreService, ArtifactsManager artifactsManager) {
		this.agentRestService = agentRestService;
		this.agentRootFolder = externalStaticFileLocation;
		this.agentServerPort = port;
		this.checkArtifacts = checkArtifacts;
		this.checkMultiArtifacts = checkMultiArtifacts;
		this.stateService = stateService;
		this.jsonStoreService = jsonStoreService;
		this.artifactsManager = artifactsManager;
		WsHandler.injectAgentRestService(this.agentRestService.get());

	}

	public void stop() {

		this.agentRestService.get().shutdown();
		WsHandler.injectAgentRestService(null);
	}

	public void start() {
		this.start(true);
	}

	public void restart() {
		this.start(false);
	}

	public void start(boolean withGUI) {
		Logger.info("starting agent ... " + this.agentRestService);
		this.agentRestService.get().startup();
		WsHandler.injectAgentRestService(this.agentRestService.get());
		this.agentRestService.get().createService(agentRootFolder, Integer.parseInt(agentServerPort));
		try {
			Logger.info("checking downloaded artifacts");
			Logger.info("done?" + this.checkArtifacts.get().start());
		} catch (Exception e) {
			Logger.info("error on checking downloaded artifacts", e);
		}
		// se presente avviare il comando di startup della GUI dell'agent
		if (withGUI) {
			try {
				// CommandRunner.runProcess(AgentApp.map.get(CLIMain.guiCmd),
				// "/esel/terminal/bin");
				CommandRunner.runProcess(AgentApp.map.get(CLIMain.guiCmd),
						System.getProperty("agent.workingDir") + "/bin");
			} catch (IOException e1) {
				Logger.error(e1, "exception during start: ");
			}
		}
		// provo a fare un download immediato prima di avviare lo scheduler
		try {
			this.checkMultiArtifacts.get().start();
			// ctx.createMultipleArtifactsJob().start();
		} catch (Exception e) {
			Logger.error(e, "exception during start: ");
			AgentState state = AgentState.ERROR;
			state.setReason(e.getMessage());
			stateService.setState(state);
		}
	}

	public void startScheduler() {
		Logger.info("Creating scheduler...");
		SundialJobScheduler.createScheduler(new SchedulerFactory());
		Map<String, Object> params = new HashMap<>();
		params.put("ArtifactsManagerStateService", stateService);
		params.put("groupId", System.getProperty("agent.artifacts.store.groupId"));
		params.put("JsonStoreService", jsonStoreService);
		params.put("ArtifactsManager", artifactsManager);

		Logger.info("Adding job scheduler...");

		// if (.getMessageReceiver() != null) {
		// SundialJobScheduler.addJob("MessageReceiverJob", MessageReceiverJob.class,
		// params, false);
		// }
		// if (ctx.getMessageReceiver() != null) {
		// SundialJobScheduler.addSimpleTrigger("MessageReceiverTrigger",
		// "MessageReceiverJob", -1, 1);
		// }
		Logger.info("Adding addCronTrigger scheduler...");
		SundialJobScheduler.addJob("UpdateJob", JobWrapper.class, params, false);

		long mezor = 60 * 1000 * 30;
		long diescsicond = 10 * 1000;
		SundialJobScheduler.addSimpleTrigger("UpdateTrigger", "UpdateJob", -1, diescsicond);

		// SundialJobScheduler.addCronTrigger("MessageReceiverTrigger",
		// "MessageReceiverJob", "0/0.5 * * * * ?");
		SundialJobScheduler.startScheduler(1);
		Logger.info("Scheduler started");
	}

	public void stopScheduler() {
		SundialJobScheduler.stopJob("UpdateJob");
		try {
			SundialJobScheduler.removeJob("UpdateJob");
			SundialJobScheduler.removeTrigger("UpdateTrigger");
			// SundialJobScheduler.getScheduler().shutdown(false);
			SundialJobScheduler.shutdown();
		} catch (Exception e) {
			Logger.error(e, "exception during stopScheduler: ");
		}

	}

}

public class AgentApp {

	static AgentComponent component;

	static Agent agent;

	static Map<String, String> map;

	@Singleton
	@Component(modules = { AgentModule.class })
	public interface AgentComponent {
		Agent agent();
	}

	public static void stopAgent() {

		DeviceFactory.get().destroyDevice();
		agent.stopScheduler();
		agent.stop();

	}

	public static void startAgent() {
		component = DaggerAgentApp_AgentComponent.builder().build();
		agent = component.agent();
		agent.start();
		agent.startScheduler();
	}

	public static void restartAgent() {
		component = DaggerAgentApp_AgentComponent.builder().build();
		agent = component.agent();
		agent.restart();
		agent.startScheduler();
	}

	private static JFrame showLoadingDialog() {
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(Color.DARK_GRAY);
		centerPanel.setForeground(Color.white);
		centerPanel.setFont(new Font("Dialog", Font.BOLD, 55));
		JLabel jl = new JLabel("Caricamento in corso....");
		jl.setBackground(Color.DARK_GRAY);
		jl.setForeground(Color.white);
		jl.setFont(new Font("Dialog", Font.BOLD, 55));
		centerPanel.add(jl);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("EmsMobile");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setSize((int) d.getWidth(), (int) d.getHeight());
		frame.setVisible(true);
		frame.setBackground(Color.black);
		frame.setForeground(Color.white);
		gd.setFullScreenWindow(frame);
		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent evt) {

			}

			public void windowIconified(WindowEvent e) {
				gd.setFullScreenWindow(frame);
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);

			}

		});

		return frame;
	}

	public static void main(String[] args) throws MalformedURLException {
		System.setProperty("agent.workingDir", "/esel/terminal/web");
		Options options = CLIMain.createOptions();
		AgentApp.map = CLIMain.buildOptions(options, args);
		if (map.size() < CLIMain.MIN_ARGS) {
			Logger.info(map);
			CLIMain.showHelp(options);
			return;
		}
		JFrame frame = null;
		if (map.get(CLIMain.loadingUI).equalsIgnoreCase("true")) {
			frame = showLoadingDialog();
		}

		Optional<String> newUrl = Optional.empty();
		newUrl = Optional.ofNullable(DiscoveryUtil.discoverNewSite());

		String remoteUrl = newUrl.isPresent() ? newUrl.get() : map.get(CLIMain.remoteUrl);

		Logger.info("Url found is: " + remoteUrl);

		System.setProperty("agent.artifacts.http.restPath", "api/rest/artifacts");
		System.setProperty("agent.artifacts.http.baseUrl", remoteUrl);
		System.setProperty("agent.apiSelector.baseUrl", remoteUrl);
		System.setProperty("agent.artifacts.store.path", map.get(CLIMain.localStorePath));
		System.setProperty("agent.artifacts.store.groupId", map.get(CLIMain.groupId));
		System.setProperty("agent.server.rootFolder", map.get(CLIMain.rootFolder));
		System.setProperty("agent.server.port", map.get(CLIMain.serverPort));

		component = DaggerAgentApp_AgentComponent.builder().build();
		agent = component.agent();
		agent.start();
		agent.startScheduler();

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				Logger.info("addShutdownHook stopping GUI...");
				try {
					//se muore l'agent spengo anche la gui
					CommandRunner.stopProcess("terminal-electron.exe", true);					
				} catch (IOException e) {
				}

			}
		}));
		if (frame != null)
			frame.setVisible(false);
	}

}
