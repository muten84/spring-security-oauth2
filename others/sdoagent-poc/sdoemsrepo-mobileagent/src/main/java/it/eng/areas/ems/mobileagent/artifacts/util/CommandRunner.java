/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.util;

import java.io.File;
import java.io.IOException;

import org.pmw.tinylog.Logger;

/**
 * @author Bifulco Luigi
 *
 */
public class CommandRunner {

	public static Process runProcess(String program, String workingDirectroy) throws IOException {
		String cmd = "cmd /c start " + program;
		//Logger.info("Executing batch: " + cmd);

		File wd = new File(workingDirectroy);
		Process proc = Runtime.getRuntime().exec(cmd, null, wd);
		return proc;
	}

	public static Process runCmdShell() throws IOException {
		return Runtime.getRuntime().exec("cmd.exe /c start", null, new File(System.getProperty("agent.workingDir")));

	}
	
	public static Process startProcess(String process, String workingDirectroy) throws IOException {
		return runProcess(process, workingDirectroy);
	}

	
	public static Process stopProcess(String processname, boolean withChild) throws IOException {
		String cmd = "taskkill /f /im "+processname;
		if(withChild) {
			cmd+= " /t ";
		}
		
		return Runtime.getRuntime().exec(cmd);
	}

	public static Process stopExplorer() throws IOException {
		return Runtime.getRuntime().exec("taskkill /f /im explorer.exe");
	}

	public static Process startExplorer() throws IOException {
		return Runtime.getRuntime().exec("explorer.exe");
	}

	public static Process restart() throws IOException {
		return Runtime.getRuntime().exec("cmd.exe /c shutdown -r");
	}

	public static Process forceShutdow() throws IOException {
		return Runtime.getRuntime().exec("cmd.exe /c shutdown -s");
	}
	
	
	public static void main(String[] args) throws IOException {
		runProcess("electron.bat", "/esel/terminal/web/bin");
	}

}
