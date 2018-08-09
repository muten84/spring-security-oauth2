package it.eng.areas.ems.sdodaeservices.entity;

public enum TipoDisponibilitaEnum {

	DISPONIBILITAH24("DISPONIBILITA' H24"), //
	NON_DEFINITA("NON DEFINITA"), //
	DA_PROGRAMMA("DA PROGRAMMA");

	private String description;

	private TipoDisponibilitaEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static TipoDisponibilitaEnum byDescription(String tipologiaDisponibilita) {
		if (tipologiaDisponibilita == null) {
			return NON_DEFINITA;
		}

		for (TipoDisponibilitaEnum en : values()) {
			if (tipologiaDisponibilita.equals(en.getDescription())) {
				return en;
			}
		}
		return NON_DEFINITA;
	}
}
