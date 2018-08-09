/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Bifulco Luigi
 *
 */
public class ArtifactsManagerStateService {

	public static enum AgentState {
		START(""),//
		IDLE(""),//
		CHECKING(""),//
		DOWNLOADING(""),//
		DOWNLOAD_SUCCESS(""),//
		DOWNLOAD_ERROR(""),//
		INSTALLING(""),//
		INSTALLED(""),//
		NOT_INSTALLED(""),//
		ERROR("");

		private String reason;

		private AgentState(String reason) {
			this.reason = reason;
		}

		public String getReason() {
			return this.reason;
		}

		public void setReason(String r) {
			this.reason = r;

		}
	}

	public ArtifactsManagerStateService(AgentState initalState) {
		state.set(initalState);
	}

	private AtomicReference<AgentState> state = new AtomicReference<>();

	public AgentState setState(AgentState newState) {
		return state.getAndSet(newState);
	}

	public AgentState getState() {
		return state.get();
	}		
	

}
