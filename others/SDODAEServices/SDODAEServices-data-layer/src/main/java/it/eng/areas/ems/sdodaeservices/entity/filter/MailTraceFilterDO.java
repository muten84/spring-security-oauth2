package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.util.Date;

public class MailTraceFilterDO {
	
	private String destinatario;
	
	private Date dataDa;

	private Date dataA;

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public Date getDataDa() {
		return dataDa;
	}

	public void setDataDa(Date dataDa) {
		this.dataDa = dataDa;
	}

	public Date getDataA() {
		return dataA;
	}

	public void setDataA(Date dataA) {
		this.dataA = dataA;
	}
	
	
}