/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.jsondb.annotation.Document;

/**
 * @author Bifulco Luigi
 *
 */
@Document(collection = "session", schemaVersion = "1.0")
public class MobileDeviceSession {

	@io.jsondb.annotation.Id
	private String sessionId;

	private List<SessionEntry> entries;

	public MobileDeviceSession() {
		entries = new LinkedList<>();
	}

	public MobileDeviceSession(String sessionId) {
		this();
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void addEntry(String key, String value) {
		this.entries.add(new SessionEntry(key, value));
	}

	public SessionEntry getEntry(String key) {
		return this.entries.stream().filter((e) -> {
			return e.getKey().equals(key);
		}).findFirst().get();
	}

	/**
	 * @return the entries
	 */
	public List<SessionEntry> getEntries() {
		return entries;
	}

	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(List<SessionEntry> entries) {
		this.entries = entries;
	}

	public static class SessionEntry {
		private String key;
		private String value;

		public SessionEntry() {
		}

		public SessionEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key
		 *            the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

	}

}
