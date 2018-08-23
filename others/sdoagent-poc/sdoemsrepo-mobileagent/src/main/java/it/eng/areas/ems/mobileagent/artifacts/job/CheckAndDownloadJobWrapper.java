/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.job;

import org.knowm.sundial.exceptions.JobInterruptException;
import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.artifacts.ExecutionContext;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;

/**
 * @author Bifulco Luigi
 *
 */
public class CheckAndDownloadJobWrapper extends org.knowm.sundial.Job {

	public CheckAndDownloadJobWrapper() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.knowm.sundial.Job#doRun()
	 */
	@Override
	public void doRun() throws JobInterruptException {
		ExecutionContext ctx = getJobContext().get("ExecutionContext");
		AgentState s = AgentState.CHECKING;
		s.setReason("");
		ctx.getArtifactsManagerStateService().setState(s);
		try {
			boolean success = ctx.createCheckAndDownloadJob().start();
			if (success) {
				AgentState idle = AgentState.IDLE;
				idle.setReason("");
				ctx.getArtifactsManagerStateService().setState(idle);
			}
		} catch (Exception e) {
			AgentState state = AgentState.ERROR;
			state.setReason(e.getMessage());
			ctx.getArtifactsManagerStateService().setState(state);
			Logger.error(e,"Exception in CheckAndDownloadJobWrapper::doRun");
			throw new JobInterruptException();
		}

	}

}
