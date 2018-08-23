package it.eng.areas.ems.sdodaeservices.entity;

public enum TipoManutenzioneEnum {

	MANUTENZIONE_ELETTRODI("ELETTRODI"), //
	MANUTENZIONE_BATTERIE("BATTERIE"), //
	MANUTENZIONE_ELETTRODI_PEDIATRICI("ELETTRODI PEDIATRICI");

	private String description;

	private TipoManutenzioneEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
