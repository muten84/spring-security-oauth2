/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import it.eng.areas.ems.ordinari.entity.ToBookingDO;
import it.eng.areas.ems.ordinari.entity.ToBookingPatientDO;
import it.eng.areas.ems.ordinari.entity.TsBookingDpiDO;
import it.eng.areas.ems.ordinari.entity.TsBookingEquipmentDO;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
public class DocumentServiceDelegateImplHelper {

	private static SimpleDateFormat DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");

	private static ToBookingPatientDO patient(ToBookingDO b) {
		Set<ToBookingPatientDO> pats = b.getBookingPatients();
		if (pats == null || pats.isEmpty()) {
			return null;
		}
		return pats.iterator().next();
	}

	private static void fillPatientData(ToBookingPatientDO pat, BookingsPrintDataSource ds) {
		if (pat == null) {
			return;
		}
		Date d = pat.getBirthDate();
		if (d != null) {
			String str = DDMMYYYY.format(d);
			ds.setBirthDate(str);
		}
		ds.setSsn(StringUtils.defaultIfEmpty(pat.getTaxCode(), ""));
		ds.setTransported(StringUtils.defaultIfEmpty(pat.getSurname(), "") + " " + StringUtils.defaultIfEmpty(pat.getName(), ""));
		ds.setBirthPlace(pat.getBirthPlace());
	}

	private static String listEquipments(ToBookingDO b) {
		Set<TsBookingEquipmentDO> list = b.getBookingEquipments();
		String sEquips = "";
		if (list != null && !list.isEmpty()) {
			for (TsBookingEquipmentDO tsBookingEquipmentDO : list) {
				if (tsBookingEquipmentDO.getEquipment() != null) {
					sEquips += tsBookingEquipmentDO.getEquipment().getDescription() + ", ";
				}
			}
		}
		Set<TsBookingDpiDO> list2 = b.getBookingDpis();
		if (list2 != null && !list2.isEmpty()) {
			for (TsBookingDpiDO tsBookingEquipmentDO : list2) {
				if (tsBookingEquipmentDO.getEquipment() != null) {
					sEquips += tsBookingEquipmentDO.getEquipment().getDescription() + ", ";
				}
			}
		}
		return sEquips;
	}

	public static BookingsPrintDataSource convertBookingsPrintDataSource(ToBookingDO b) {
		BookingsPrintDataSource ds = new BookingsPrintDataSource();
		ToBookingPatientDO pat = patient(b);
		fillPatientData(pat, ds);

		ds.setCategory(StringUtils.defaultIfEmpty(b.getCategory(), "-"));
		ds.setCompanion(StringUtils.defaultIfEmpty(b.getCompanion(), "-"));
		ds.setConvention(StringUtils.defaultIfEmpty(b.getReqConvention(), "-"));
		ds.setDeambulation(StringUtils.defaultIfEmpty(b.getPatStatus(), "-"));// deambulazione
		ds.setEquipmentList(listEquipments(b));
		ds.setNote(StringUtils.defaultIfEmpty(b.getNote(), "-"));
		ds.setPriority(StringUtils.defaultIfEmpty(b.getPriority(), "-"));
		ds.setRequestorOf(StringUtils.defaultIfEmpty(b.getReqDescr(), "-"));
		ds.setTransportDate(b.getTransportDate());
		ds.setTransportType(StringUtils.defaultIfEmpty(b.getTransportType(), "-"));
		ds.setVehicleType(StringUtils.defaultIfEmpty(b.getResourceType(), "-"));
		ds.setBookingCode(StringUtils.defaultIfEmpty(b.getCode(), "-"));
		ds.setFromPlace(StringUtils.defaultIfEmpty(b.getCompactAddress(), "-"));
		ds.setToPlace(StringUtils.defaultIfEmpty(b.getCompactAddress2(), "-"));
		ds.setReqConvention(StringUtils.defaultIfEmpty(b.getReqConvention(), "-"));
		return ds;
	}
}
