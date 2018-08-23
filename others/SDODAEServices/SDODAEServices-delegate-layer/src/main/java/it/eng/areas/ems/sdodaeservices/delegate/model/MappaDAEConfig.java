package it.eng.areas.ems.sdodaeservices.delegate.model;
public class MappaDAEConfig {

	private String id;

	private Integer taggiornamento;

	private Integer decimaliMappeGPS;

	private Integer zoomInizialeMappa;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTaggiornamento() {
		return taggiornamento;
	}

	public void setTaggiornamento(int taggiornamento) {
		this.taggiornamento = taggiornamento;
	}

	public int getDecimaliMappeGPS() {
		return decimaliMappeGPS;
	}

	public void setDecimaliMappeGPS(int decimaliMappeGPS) {
		this.decimaliMappeGPS = decimaliMappeGPS;
	}

	public int getZoomInizialeMappa() {
		return zoomInizialeMappa;
	}

	public void setZoomInizialeMappa(int zoomInizialeMappa) {
		this.zoomInizialeMappa = zoomInizialeMappa;
	}

	
	
}
