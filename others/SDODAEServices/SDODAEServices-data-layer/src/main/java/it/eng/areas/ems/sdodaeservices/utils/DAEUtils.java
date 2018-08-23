package it.eng.areas.ems.sdodaeservices.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneDO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;

@Component
public class DAEUtils {

	@Autowired
	protected DTOService dtoService;

	public DaeHistoryDO copyToHistory(DaeDO dae, UtenteDO utente, Operation operation) {

		DaeHistoryDO toRet = dtoService.convertObject(dae, DaeHistoryDO.class);

		toRet.setDaeId(dae.getId());
		toRet.setId(null);

		toRet.setUtente(utente);
		toRet.setOperationDate(new Date());
		toRet.setOperation(operation);

		return toRet;
	}

	public ProgrammaManutenzioneHistoryDO copyProgrammaToHistory(ProgrammaManutenzioneDO p, UtenteDO user) {
		ProgrammaManutenzioneHistoryDO hist = new ProgrammaManutenzioneHistoryDO();

		hist.setProgrammaManutenzione(p);
		hist.setOperation(Operation.MODIFY);
		hist.setUtente(user);
		hist.setOperationDate(new Date());
		hist.setDae(p.getDae());
		hist.setIntervallotraInterventi(p.getIntervallotraInterventi());
		hist.setDurata(p.getDurata());
		hist.setScadenzaDopo(p.getScadenzaDopo());
		hist.setResponsabile(p.getResponsabile());
		hist.setStato(p.getStato());
		hist.setNota(p.getNota());
		hist.setTipoManutenzione(p.getTipoManutenzione());
		hist.setMailAlert(p.getMailAlert());

		return hist;
	}

}
