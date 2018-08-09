/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.audio;

import java.io.File;
import java.util.Observer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.device.IAudio;

/**
 * @author Bifulco Luigi
 *
 */
public class AudioServiceImpl implements IAudio {
	private String fileUrl;

	private Clip clip;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#getName()
	 */

	public String getName() {
		return "AUDIO_SERVICE";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#addObserver(java.util.Observer)
	 */

	public void addObserver(Observer observer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#remObserver(java.util.Observer)
	 */

	public void remObserver(Observer observer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#config(it.esel.terminal.util.cfg.
	 * Configuration)
	 */

	public void config(String fileUrl) {
		this.fileUrl = fileUrl;
		
		// .getParameterValue("GENERAL", "BEEP_SOUND", "etc/beep.wav");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#isRunning()
	 */

	public boolean isRunning() {
		return clip.isRunning();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#startup()
	 */

	public boolean startup() {
		if (fileUrl == null || fileUrl.isEmpty()) {
			return false;
		}
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileUrl));
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(6.0206f); // Reduce volume by 10 decibels.
			clip.setLoopPoints(0, -1);
			// clip.start();
		} catch (Exception e) {
			Logger.error(e,"startup AudioServiceImpl exception ");
			throw new RuntimeException(e.getMessage());
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#shutdown(it.esel.terminal.svc.msg.
	 * LateResult)
	 */

	@Override
	public void shutdown() {
		if (clip != null) {
			clip.close();
			this.clip = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#restart()
	 */

	public void restart() {
		this.clip.setFramePosition(0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#suspend()
	 */

	@Override
	public void suspend() {
		if (clip.isRunning()) {
			clip.stop();
		}
		this.restart();
		
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.esel.terminal.svc.IService#resume()
	 */

	@Override
	public void resume() {
		if (!clip.isRunning())
			clip.start();
	}

	@Override
	public void playSound() {
		new Thread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */

			public void run() {
				synchronized (clip) {
					if (!isRunning()) {
						resume();
					}
				}

			}
		});
	}

	public void stopSound() {
		if (isRunning()) {
			suspend();
		}
	}

}
