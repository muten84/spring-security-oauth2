package it.eng.areas.ems.mobileagent.jnative.win32.gps;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.pmw.tinylog.Logger;

public class NMEAParser {

	public boolean parse(String sentence, GpsInfo info) throws NMEAParseException {
		if (sentence == null || sentence.length() == 0) {
			return false;
		}
		
		String[] sentenceTokens = StringUtils.split(sentence, ",*");
		if (sentenceTokens != null && sentenceTokens.length > 0) {
			//System.out.println(sentenceTokens[0]+" - "+sentence);
			if ("$GPGGA".equals(sentenceTokens[0])) {
				try {
					return parseGGA(sentenceTokens, info);
				} catch (Exception e) {
					Logger.error(e,"GGA sentence parsing failure : " + sentence);
					throw new NMEAParseException(e.getMessage());
				}
			} else if ("$GPRMC".equals(sentenceTokens[0])) {
				try {
					return parseRMC(sentenceTokens, info);
				} catch (Exception e) {
					Logger.error(e,"RMC sentence parsing failure : " + sentence);
					throw new NMEAParseException(e.getMessage());
				}
			}
		}
		return false;
	}
	
	private String getSentenceField(int position, String[] sentence) {
		if(sentence == null || sentence.length<=0 || sentence.length<=position) {
			return "0";
		}
		return sentence[position];
	}

	private boolean parseGGA(String[] sentence, GpsInfo info) {
		int idx = 1;
		
		info.time = getSentenceField(idx++, sentence);
		info.latitude = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.latitudeAge = 0;
		info.hemisph =  getSentenceField(idx++, sentence);
		info.hemisphAge = 0;
		info.longitude = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.longitudeAge = 0;
		info.direction =  getSentenceField(idx++, sentence);
		info.directionAge = 0;
		info.quality = NumberUtils.toInt( getSentenceField(idx++, sentence));
		info.qualityAge = 0;
		info.satellites = NumberUtils.toInt( getSentenceField(idx++, sentence));
		info.hdop = NumberUtils.toFloat( getSentenceField(idx++, sentence));
		info.hdopAge = 0;
		info.height = NumberUtils.toFloat( getSentenceField(idx++, sentence));
		info.heightAge = 0;
		info.heightUnit =  getSentenceField(idx++, sentence);
		info.geoidalHeight = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.geoidalHeightAge = 0;
		info.geoidalHeightUnit =  getSentenceField(idx++, sentence);
		info.diffCorrection = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.diffCorrectionAge = 0;
		info.diffStationID = NumberUtils.toInt( getSentenceField(idx++, sentence));

		return true;
	}

	private boolean parseRMC(String[] sentence, GpsInfo info) {
		int idx = 1;

		info.time =  getSentenceField(idx++, sentence);
		info.status =  getSentenceField(idx++, sentence);
		info.latitude = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.latitudeAge = 0;
		info.hemisph =  getSentenceField(idx++, sentence);
		info.hemisphAge = 0;
		info.longitude = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.longitudeAge = 0;
		info.direction =  getSentenceField(idx++, sentence);
		info.directionAge = 0;
		info.speed = NumberUtils.toFloat( getSentenceField(idx++, sentence)) * 1.8520000000035852f;
		info.speedAge = 0;
		info.course = NumberUtils.toFloat( getSentenceField(idx++, sentence));
		info.courseAge = 0;
		info.date =  getSentenceField(idx++, sentence);
		info.magnVariation = NumberUtils.toDouble( getSentenceField(idx++, sentence));
		info.magnVarDirection =  getSentenceField(idx++, sentence);

		return true;
	}

	public String checkSum(String msg) {
		int chk = 0;
		for (int i = 1; i < msg.length(); i++) {
			chk ^= msg.charAt(i);
		}

		String chk_s = Integer.toHexString(chk).toUpperCase();

		while (chk_s.length() < 2) {
			chk_s = "0" + chk_s;
		}

		return chk_s;
	}

	public boolean verifyChecksum(String msg) {
		int msglen = (msg != null) ? msg.length() : 0;
		if (msglen > 4) {
			if (msg.charAt(msglen - 3) == '*') {
				String chk_s = checkSum(msg.substring(0, msglen - 3));
				return (msg.substring(msglen - 2, msglen).equals(chk_s));
			} else {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws NMEAParseException {
		String sentence = "$GPRMC,142.000,N,00000.000,E,000.0,000.0,110817,000.0,E*7B";
		NMEAParser parser = new NMEAParser();
		GpsInfo info = new GpsInfo();
		parser.parse(sentence, info);

	}

}
