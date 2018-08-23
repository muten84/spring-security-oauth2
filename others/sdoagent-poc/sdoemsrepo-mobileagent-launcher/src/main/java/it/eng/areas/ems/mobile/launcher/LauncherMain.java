/**
 * 
 */
package it.eng.areas.ems.mobile.launcher;

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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Bifulco Luigi
 *
 */
public class LauncherMain {
	private final static String artifacts = "artifacts";
	private final static String cmd = "cmd";
	private final static String cwd = "cwd";
	private final static String storePath = "sp";

	private static Map<String, String> inputs = new HashMap<>();
	private static JFrame frame;

	public static void main(String[] args) throws IOException {

		// lockUI
		//frame = lockUI();

		// collect those inputs:
		// - local working directory (cwd path)
		// - artifacts to check (comma seprated list)
		// - command to launch (batch present in bin folder of cwd path)
		if (!checkInput(args, 4)) {
			throw new IllegalArgumentException(
					"you have to pass at least 4 arguments in the order: artifacts, cmd, cwd, localStorePath");
		}

		// for each artifact to check, verify if there is something locally to update
		String[] arts = inputs.get(artifacts).split(",");
		for (String a : arts) {
			String source = checkArtifact(a);
			// if so unzip and copy to destination url
			String destination = "/esel/terminal/web/mobile-agent";
			if (source != null) {
				boolean done = installArtifact(source, destination);
				System.out.println("installation done: " + done);
			}
		}

		launcCommand(inputs.get(cmd), inputs.get(cwd));
		// in any case launch passed command

		// unlockUI
		//unlockUI();
	}

	/**
	 * 
	 * @param args
	 * @return true if the input args are valid
	 */
	private static boolean checkInput(String[] args, int verifyLen) {
		// populate the map
		if (args == null || args.length < verifyLen) {
			return false;
		}
		inputs.put(artifacts, args[0]);
		inputs.put(cmd, args[1]);
		inputs.put(cwd, args[2]);
		inputs.put(storePath, args[3]);

		return true;
	}

	/**
	 * check there is an artifact to install with artifactId in localstore path
	 * folder
	 * 
	 * @param artifactId
	 * @return the string representing the path were is the file to install
	 * @throws IOException
	 */
	private static String checkArtifact(String artifactId) throws IOException {
		System.out.println("checking artifacts...");
		File f = new File(inputs.get(storePath));
		if (f.exists()) {
			File[] files = f.listFiles();
			for (File file : files) {
				String s = file.getName();

				if (s.contains(artifactId) && s.endsWith("zip")) {
					// return f.getCanonicalPath();
					return file.toPath().normalize().toString();
				}
			}

		}
		return null;
	}

	/**
	 * 
	 * @param artifactId
	 * @param destination
	 * @return true if the checked artifact was installed correctly
	 * @throws IOException
	 */
	private static boolean installArtifact(String source, String destination) throws IOException {

		System.out.println("installing: " + source + " to-> " + destination);
		InstallUtils.backupFolder("/esel/terminal/download/backup/mobile_agent", destination);
		boolean done = InstallUtils.extractAndCopy(source, destination);
		if (done) {
			return InstallUtils.installationDone(source);
		}
		return done;
	}

	/**
	 * lock UI with a message notice the update phase
	 */
	private static JFrame lockUI() {
		System.out.println("locking ui...");

		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBackground(Color.DARK_GRAY);
		centerPanel.setForeground(Color.white);
		centerPanel.setFont(new Font("Dialog", Font.BOLD, 55));
		JLabel jl = new JLabel("Aggiornamento applicazione in corso...");
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

		frame.setBackground(Color.black);
		frame.setForeground(Color.white);

		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent evt) {

			}

			public void windowIconified(WindowEvent e) {
				gd.setFullScreenWindow(frame);
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);

			}

		});

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				System.out.println("showing window...");
				gd.setFullScreenWindow(frame);
				frame.setVisible(true);

			}
		});

		return frame;
	}

	/**
	 * unlock the UI
	 */
	private static void unlockUI() {
		SwingUtilities.invokeLater(new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				System.out.println("closing window...");
				if (frame != null)
					frame.setVisible(false);

			}
		});

	}

	/**
	 * launchs a batch named with cmd param in the bin folder of cwd
	 * 
	 * @param cmd
	 * @param cwd
	 * @throws IOException
	 */
	private static void launcCommand(String cmd, String cwd) throws IOException {
		System.out.println("launching command: ..." + cmd + " - " + cwd);
		Process p = runProcess(cmd, cwd);
		System.exit(0);
	}

	public static Process runProcess(String program, String workingDirectroy) throws IOException {
		String cmd = "cmd /c start " + program;
		System.out.println("Executing batch: " + cmd);

		File wd = new File(workingDirectroy);
		Process proc = Runtime.getRuntime().exec(cmd, null, wd);
		return proc;
	}

}
