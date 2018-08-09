package it.eng.areas.ems.sdodaeservices.delegate.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.entity.GiornoSettimanaEnum;
import it.eng.areas.ems.sdodaeservices.entity.TipoDisponibilitaEnum;

public class DaeUtils {

	public static Map<String, String> getOrariMap(Dae d) {

		Map<String, String> orari = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return GiornoSettimanaEnum.valueOf(o1).compareTo(GiornoSettimanaEnum.valueOf(o2));
			}
		});

		Map<GiornoSettimanaEnum, Set<String>> orariD = new TreeMap<>();

		d.getDisponibilita().forEach(disp -> {
			if (disp.getDisponibilitaSpecifica() != null
					&& disp.getDisponibilitaSpecifica().getDisponibilitaGiornaliera() != null
					&& disp.getDisponibilitaSpecifica().getDisponibilitaGiornaliera().size() > 0) {

				// aggiungo i giorni alla stringa
				disp.getDisponibilitaSpecifica().getDisponibilitaGiornaliera().forEach(dg -> {
					Set<String> actual = orariD.get(dg.getGiornoSettimana());

					if (actual == null) {
						actual = new TreeSet<>();
						orariD.put(dg.getGiornoSettimana(), actual);
					}

					actual.add(dg.getOrarioDa() + "-" + dg.getOrarioA());
				});
			}
		});

		orariD.forEach((k, v) -> {
			orari.put(k.name(), String.join(" / ", v));
		});

		return orari;
	}

	public static String getOrariString(Dae d) {
		TipoDisponibilitaEnum tipoDisp = TipoDisponibilitaEnum.byDescription(d.getTipologiaDisponibilita());

		switch (tipoDisp) {
		case DISPONIBILITAH24:
			return "DISPONIBILITA H24";
		case NON_DEFINITA:
			return "DISPONIBILITA NON DEFINITA";
		case DA_PROGRAMMA:
			Map<String, String> map = getOrariMap(d);

			return String.join(" ", map.entrySet().stream().map((e) -> {
				return e.getKey() + ":" + e.getValue();
			}).collect(Collectors.toList()));
		}
		return "";

	}

	public static String getIndirizzo(Dae dae) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(dae.getGpsLocation().getIndirizzo().getName());
		buffer.append(" ");
		buffer.append(dae.getGpsLocation().getCivico());
		buffer.append(" - ");
		buffer.append(dae.getGpsLocation().getComune().getNomeComune());

		return buffer.toString();
	}

}
