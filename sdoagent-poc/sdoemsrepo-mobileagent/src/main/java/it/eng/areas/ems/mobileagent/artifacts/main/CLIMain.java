/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.knowm.sundial.SundialJobScheduler;
import org.pmw.tinylog.Logger;
import org.quartz.core.SchedulerFactory;

import it.eng.areas.ems.mobileagent.artifacts.ExecutionContext;
import it.eng.areas.ems.mobileagent.artifacts.job.CheckDownloadedArtifacts;
import it.eng.areas.ems.mobileagent.artifacts.job.JobWrapper;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.util.CommandRunner;
import it.eng.areas.ems.mobileagent.artifacts.util.InstallUtils;
import it.eng.areas.ems.mobileagent.http.AgentRestService;
import it.eng.areas.ems.mobileagent.message.job.MessageReceiverJob;

/**
 * @author Bifulco Luigi
 *
 */
public class CLIMain {

	public static int MIN_ARGS = 5;
	public static String remoteUrl = "remoteUrl";
	public static final String mqttUrl = "mqttUrl";
	public static final String groupId = "groupId";
	// private static final String artifactId = "artifactId";
	public static final String localStorePath = "localStorePath";
	public static final String rootFolder = "rootFolder";
	public static final String guiCmd = "guidCmd";
	public static final String serverPort = "serverPort";
	public static final String loadingUI = "loadingUI";

	public static Map<String, String> map;

	public static void main(String[] args) throws IOException {
		start(args);
	}

	/**
	 * @param args
	 * @throws IOException
	 * 
	 */
	public static void start(String[] args) throws IOException {

		Options options = createOptions();
		map = buildOptions(options, args);
		if (map.size() < MIN_ARGS) {
			//Logger.info(map);
			showHelp(options);
			return;
		}

		try {
			// Luigi commentato perché lo deve fare solo on demand:
			// Logger.info("clear app cache: " +
			InstallUtils.clearAppCache("terminal-electron");
		} catch (Exception e) {

		}

		ExecutionContext ctx = new ExecutionContext();
		ctx = ctx.createExecutionContext(map.get(remoteUrl), map.get(mqttUrl), map.get(groupId), "artifactId",
				map.get(localStorePath), Integer.parseInt(map.get(serverPort)));
		// AgentRestService.createService(root);
		// TODO: installare eventuali artefatti già scaricati e non ancora
		// installati
		try {
			//Logger.info("checking downloaded artifacts");
			// Logger.info("done?" + new CheckDownloadedArtifacts().start());
		} catch (Exception e) {
			e.printStackTrace();
			//Logger.info("error on checking downloaded artifacts");
		}
		// se presente avviare il comando di startup della GUI dell'agent
		CommandRunner.runProcess(map.get(guiCmd), System.getProperty("agent.workingDir") + "/bin");
		// provo a fare un download immediato prima di avviare lo scheduler
		try {
			ctx.createMultipleArtifactsJob().start();
		} catch (Exception e) {
			AgentState state = AgentState.ERROR;
			state.setReason(e.getMessage());
			ctx.getArtifactsManagerStateService().setState(state);
		}
		startScheduler(ctx);

	}

	public static void startScheduler(ExecutionContext ctx) {
		//Logger.info("Creating scheduler...");
		SundialJobScheduler.createScheduler(new SchedulerFactory());
		Map<String, Object> params = new HashMap<>();
		params.put("ExecutionContext", ctx);
		//Logger.info("Adding job scheduler...");
		SundialJobScheduler.addJob("UpdateJob", JobWrapper.class, params, false);
		// if (ctx.getMessageReceiver() != null) {
		// SundialJobScheduler.addJob("MessageReceiverJob", MessageReceiverJob.class,
		// params, false);
		// }
		//Logger.info("Adding addCronTrigger scheduler...");
		SundialJobScheduler.addCronTrigger("UpdateTrigger", "UpdateJob", "0/50 * * * * ?");
		if (ctx.getMessageReceiver() != null) {
			SundialJobScheduler.addSimpleTrigger("MessageReceiverTrigger", "MessageReceiverJob", -1, 1);
		}
		// SundialJobScheduler.addCronTrigger("MessageReceiverTrigger",
		// "MessageReceiverJob", "0/0.5 * * * * ?");
		SundialJobScheduler.startScheduler(1);
		//Logger.info("Scheduler started");
	}

	public static Map<String, String> buildOptions(Options options, String[] args) {
		// create the command line parser
		CommandLineParser parser = new GnuParser();
		Map<String, String> map = new HashMap<>();

		try {

			CommandLine line = parser.parse(options, args);

			if (line.hasOption("r")) {
				map.put(remoteUrl, line.getOptionValue(remoteUrl));
			}
			if (line.hasOption("m")) {
				map.put(mqttUrl, line.getOptionValue(mqttUrl));
			}
			if (line.hasOption("g")) {
				map.put(groupId, line.getOptionValue(groupId));
			}
			if (line.hasOption("a")) {
				map.put("artifactId", line.getOptionValue("artifactId"));
			}
			if (line.hasOption("l")) {
				map.put(localStorePath, line.getOptionValue(localStorePath));
			}
			if (line.hasOption("f")) {
				map.put(rootFolder, line.getOptionValue(rootFolder));
			}
			if (line.hasOption("i")) {
				map.put(guiCmd, line.getOptionValue(guiCmd));
			}
			if (line.hasOption("p")) {
				map.put(serverPort, line.getOptionValue(serverPort));
			}
			if (line.hasOption("u")) {
				map.put(loadingUI, String.valueOf(true));
			} else {
				map.put(loadingUI, String.valueOf(false));
			}

		} catch (ParseException exp) {
			//Logger.info("Unexpected exception:" + exp.getMessage());
		}
		return map;

	}

	public static Options createOptions() {
		// create the Options
		Options options = new Options();
		options.addOption("r", remoteUrl, true, "the remote URL of ARTIFACTS MANAGER");
		options.addOption("f", rootFolder, true, "the local root folder exposed by the agent");
		options.addOption("m", mqttUrl, true, "the remote URL of MQTT MESSAGE BROKER");
		options.addOption("g", groupId, true, "the groupId to scan");
		options.addOption("a", "artifactId", true, "the artifactId to check update");
		options.addOption("l", localStorePath, true, "the local store in which file will be stored");
		options.addOption("p", serverPort, true, "the server port to use when invoke http rest service");
		options.addOption("i", guiCmd, true,
				"the gui cmd to launch after artifacts check installation and before artifacts check scheduling");
		options.addOption("u", loadingUI, false, "pass this option to show a loading div");

		return options;
	}

	public static void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("mobile-agent", options);
	}

}
