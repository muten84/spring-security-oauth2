/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.job;

import org.knowm.sundial.exceptions.JobInterruptException;
import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;

/**
 * @author Bifulco Luigi
 *
 */
public class JobWrapper extends org.knowm.sundial.Job {

	private ArtifactsManagerStateService stateService;
	private String groupId;
	private JsonStoreService jsonStoreService;
	private String localStorePath;
	private ArtifactsManager artifactsManager;

	public JobWrapper() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.knowm.sundial.Job#doRun()
	 */
	@Override
	public void doRun() throws JobInterruptException {
		// TODO: ExecutionContext
		Logger.info("JobWrapper....");
		// ExecutionContext ctx = getJobContext().get("ExecutionContext");
		stateService = getJobContext().get("ArtifactsManagerStateService");
		groupId = getJobContext().get("groupId");
		jsonStoreService = getJobContext().get("JsonStoreService");
		artifactsManager = getJobContext().get("ArtifactsManager");		
		localStorePath =System.getProperty("agent.artifacts.store.path");
//		if(stateService.getState() != AgentState.IDLE) {
//			Logger.warn("warning state service is not idle: "+stateService.getState());
//			return;
//		}
		AgentState s = AgentState.CHECKING;
		s.setReason("");
		stateService.setState(s);
		try {
			boolean success = createMultipleArtifactsJob().start();
			if (success) {
				Logger.info("JobWrapper done? " + success);
				AgentState idle = AgentState.IDLE;
				idle.setReason("");
				stateService.setState(idle);
			}
		} catch (Exception e) {
			Logger.error(e,"Exception in JobWrapper::doRun");
			AgentState state = AgentState.ERROR;
			state.setReason(e.getMessage());
			stateService.setState(state);
			throw new JobInterruptException();
		}
		AgentState idle = AgentState.IDLE;
		idle.setReason("");
		stateService.setState(idle);

	}

	public MultipleArtifactsJob createMultipleArtifactsJob() {
		return new MultipleArtifactsJob(this.groupId, this.localStorePath, this.artifactsManager,
				this.jsonStoreService,this.stateService);
	}

}
