package it.eng.areas.ems.sdodaeservices.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;

public class FRUtils {

	static DateFormat format = new SimpleDateFormat("HH:mm");

	public static boolean isDisponibile(FirstResponderDO fr) {

		return evaluate(fr, Arrays.asList(FRUtils::checkStato, FRUtils::checkSilenzioso, FRUtils::checkNonDisturbare));
	}

	public static boolean evaluate(final FirstResponderDO fr, List<Function<FirstResponderDO, Boolean>> functions) {
		return functions.stream().allMatch(f -> f.apply(fr));
	}

	public static Boolean checkStato(FirstResponderDO fr) {
		return fr.getStatoProfilo() == FRStatoProfiloEnum.ATTIVO;
	}

	public static Boolean checkSilenzioso(FirstResponderDO fr) {
		if (fr.getSilent() != null && fr.getSilent()) {
			String actualDate = format.format(new Date());
			if (fr.getSilentFrom().compareTo(fr.getSilentTo()) <= 0) {
				return fr.getSilentFrom().compareTo(actualDate) > 0 || fr.getSilentTo().compareTo(actualDate) < 0;
			} else {
				return fr.getSilentFrom().compareTo(actualDate) < 0 && fr.getSilentTo().compareTo(actualDate) > 0;
			}

		}
		return true;
	}

	public static Boolean checkNonDisturbare(FirstResponderDO fr) {
		if (fr.getDisponibile() != null && fr.getDisponibile()) {
			String actualDate = format.format(new Date());
			if (fr.getDoNotDisturbFrom().compareTo(fr.getDoNotDisturbTo()) <= 0) {
				return fr.getDoNotDisturbFrom().compareTo(actualDate) > 0
						|| fr.getDoNotDisturbTo().compareTo(actualDate) < 0;
			} else {
				return fr.getDoNotDisturbFrom().compareTo(actualDate) < 0
						&& fr.getDoNotDisturbTo().compareTo(actualDate) > 0;
			}
		}
		return true;
	}

}
