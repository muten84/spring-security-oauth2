package it.eng.areas.ems.sdodaeservices.rest.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.area118.sdogis.model.v_1_0_0.Address;
import it.eng.area118.sdogis.model.v_1_0_0.GeocodingPrecisionEnum;
import it.eng.area118.sdogis.model.v_1_0_0.GeocodingResult;
import it.eng.area118.sdogis.model.v_1_0_0.Point;
import it.eng.area118.sdogis.model.v_1_0_0.ProjectionsEnum;
import it.eng.area118.sdogis.model.v_1_0_0.ReverseGeocodeResult;
import it.eng.area118.sdogis.model.v_1_0_0.exception.GeoException;
import it.eng.area118.sdogis.service.GeocodingService;
import it.eng.area118.sdogis.service.ws.stub.GeocodingService_Service;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.LocalitaDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StradeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.Localita;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.LocalitaFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.rest.model.ReverseGeocodingResult;

@Component
public class GeocodeService {

	private GeocodingService_Service gs;

	private GeocodingService geocodeService;

	@Autowired
	protected AnagraficheDelegateService anagraficheDelegateService;

	private Logger logger = LoggerFactory.getLogger(GeocodeService.class);

	public GeocodingResult geocodeAddress(Address address) throws MalformedURLException, GeoException {
		return getGeocodeService().geocodeAddress(address, ProjectionsEnum.WGS_84, false);
	}

	public ReverseGeocodingResult reverseGeocoding(GPSLocation location) throws MalformedURLException, GeoException {
		Point pt = new Point();

		pt.setY(location.getLatitudine());
		pt.setX(location.getLongitudine());
		pt.setProjection(4326);
		List<ReverseGeocodeResult> res = getGeocodeService().reverseGeocode(pt, 100000, ProjectionsEnum.WGS_84);

		Strade ret = null;
		if (res != null && res.size() > 0) {
			ret = getStreetResult(res);
		}
		Localita loc = null;
		if (res != null && res.size() > 0) {
			loc = getLocalityResult(res);
		}
		ReverseGeocodingResult rev = new ReverseGeocodingResult();
		rev.setStrada(ret);
		rev.setLocalita(loc);
		return rev;

	}

	private Localita getLocalityResult(List<ReverseGeocodeResult> res) {
		for (ReverseGeocodeResult reverseGeocodeResult : res) {
			if (reverseGeocodeResult.getPrecision().equals(GeocodingPrecisionEnum.LOCALITY)) {

				logger.info("Locality :" + reverseGeocodeResult.getDescription() + " municipality "
						+ reverseGeocodeResult.getMunicipality());

				LocalitaFilter filter = new LocalitaFilter();
				filter.setName(reverseGeocodeResult.getDescription());
				filter.setNomeComune(reverseGeocodeResult.getMunicipality());
				filter.setFetchRule(LocalitaDeepDepthRule.NAME);
				List<Localita> localita = anagraficheDelegateService.searchLocalitaByFilter(filter);
				if (localita.size() > 0) {
					return localita.get(0);
				}
			}
		}
		return null;
	}

	private Strade getStreetResult(List<ReverseGeocodeResult> res) {
		for (ReverseGeocodeResult reverseGeocodeResult : res) {
			if (reverseGeocodeResult.getPrecision().equals(GeocodingPrecisionEnum.STREET)) {
				logger.info("Street :" + reverseGeocodeResult.getDescription() + " municipality "
						+ reverseGeocodeResult.getMunicipality());

				StradeFilter filter = new StradeFilter();
				filter.setName(reverseGeocodeResult.getDescription());
				filter.setNomeComune(reverseGeocodeResult.getMunicipality());
				filter.setFetchRule(StradeDeepDepthRule.NAME);
				List<Strade> strade = anagraficheDelegateService.searchStradeByFilter(filter);
				if (strade.size() > 0) {
					return strade.get(0);
				}

			}
		}
		return null;
	}

	private GeocodingService getGeocodeService() throws MalformedURLException {
		String urlStr = anagraficheDelegateService.getParameter(ParametersEnum.REVERSE_GEOCODING_SERVICE.name());
		if (!StringUtils.isEmpty(urlStr)) {
			URL baseUrl;
			baseUrl = GeocodingService_Service.class.getResource(".");
			URL url = new URL(baseUrl, urlStr);
			if (gs == null) {
				gs = new GeocodingService_Service(url,
						new QName("http://gis.area118.eng.it/sdogis/services", "GeocodingService"));
			}
			geocodeService = gs.getGeocodingServicePort();
		}
		return geocodeService;
	}
}
