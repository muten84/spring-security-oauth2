/**
 * 
 */
package it.eng.areas.ems.mobileagent.message.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;

//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;

import io.jsondb.JsonDBTemplate;
import it.eng.areas.ems.mobileagent.device.DeviceInfoUtil;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ConfigurationSection;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileDeviceSession;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileResource;

/**
 * @author Bifulco Luigi
 *
 */

// @RunWith(JUnit4.class)
public class DocumentStore {

	private JsonDBTemplate db;

	public DocumentStore() {
		// this.db = new JsonDBTemplate(System.getProperty("agent.workingDir") +
		// "/db/json",
		// "it.eng.areas.ems.sdoemsrepo.delegate.model", null);
		this.init();
	}

	public void close() {
		this.db = null;
	}

	public void init() {
		if (this.db == null) {
			this.db = new JsonDBTemplate(System.getProperty("agent.workingDir") + "/db/json",
					"it.eng.areas.ems.sdoemsrepo.delegate.model", null);
			if (!this.db.collectionExists(Message.class)) {
				Logger.info("collection not exists...");
				this.db.createCollection(Message.class);
			}
			if (!this.db.collectionExists(MobileResource.class)) {
				Logger.info("collection not exists...");
				this.db.createCollection(MobileResource.class);
			}
			if (!this.db.collectionExists(ConfigurationSection.class)) {
				Logger.info("collection not exists...");
				this.db.createCollection(ConfigurationSection.class);
			}

			if (!this.db.collectionExists(MobileDeviceSession.class)) {
				Logger.info("collection not exists...");
				this.db.createCollection(MobileDeviceSession.class);
			}

			if (this.db.collectionExists(Message.class)) {
				if (this.db.isCollectionReadonly(Message.class)) {
					Logger.info("collection exists and is readonly");
					// this.db.updateCollectionSchema(new CollectionSchemaUpdate(),
					// Message.class);
					this.db.backup(System.getProperty("agent.workingDir") + "/db/json/backup");

				}
			}
		}
	}

	public MobileDeviceSession getSession(String sessionId) {
		return this.db.findById(sessionId, MobileDeviceSession.class);
	}

	public void saveSession(MobileDeviceSession updatedSession) {
		MobileDeviceSession oldSession = this.getSession(updatedSession.getSessionId());
		if (oldSession == null) {
			this.db.insert(updatedSession);
		} else {
			oldSession.setEntries(updatedSession.getEntries());
			this.db.upsert(oldSession);
		}
	}

	public void addResource(MobileResource resource) {
		this.db.insert(resource);
	}

	public void addResourceList(Collection<MobileResource> resources) {
		this.db.insert(resources, MobileResource.class);
	}

	public void updateConfigurationDocument(List<ConfigurationSection> sections) {
		this.db.dropCollection(ConfigurationSection.class);
		this.db.createCollection(ConfigurationSection.class);
		this.db.insert(sections, ConfigurationSection.class);
	}

	public List<ConfigurationSection> getLocalConfiguration() {
		return this.db.findAll(ConfigurationSection.class);
	}

	public void updateResourceList(Collection<MobileResource> resources, String type) {
		List<MobileResource> toSave = new ArrayList<>();
		List<String> types = this.listDistinctResourceType();
		for (String t : types) {
			if (!t.equals(type)) {
				toSave.addAll(listResourceByType(t));
			}
		}
		this.db.dropCollection(MobileResource.class);
		this.db.createCollection(MobileResource.class);
		toSave.addAll(resources);
		this.addResourceList(toSave);
	}

	public List<MobileResource> listResourceByType(String type) {
		return this.db.find("/.[type='" + type + "']", MobileResource.class);
	}

	public List<String> listDistinctResourceType() {
		List<MobileResource> list = this.db.findAll(MobileResource.class);
		return list.stream().filter(distinctByKey(MobileResource::getType)).map((m) -> {
			return m.getType();
		}).collect(Collectors.toList());
	}

	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	public void storeNewMessage(Message instance) {
		this.db.insert(instance);
	}

	public long count() {
		return this.db.findAll(Message.class).size();
	}

	public void setMessageProcessed(String messageId) {
		Message m = this.db.findById(messageId, Message.class);
		m.setProcessed(true);
		this.db.save(m, Message.class);
	}

	public void setMessageNotProcessed(String messageId) {
		Message m = this.db.findById(messageId, Message.class);
		m.setProcessed(false);
		this.db.save(m, Message.class);
	}

	public Message findNextMessageToProcess() {

		List<Message> messages = db.find("/.[processed=0]", Message.class);
		Collections.sort(messages, new Comparator<Message>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(Message o1, Message o2) {
				long x = o1.getTimestamp();
				long y = o2.getTimestamp();
				// return new Long(o2.getTimestamp()).compareTo(new
				// Long(o1.getTimestamp()));

				return (x < y) ? -1 : ((x == y) ? 0 : 1);
			}
		});
		return messages.get(0);
	}

	public List<Message> listAllMessages() {
		return this.db.findAll(Message.class);
	}

	public boolean resetDocumentStore(String type) {
		switch (type) {
		case "RESOURCE":
			this.db.dropCollection(MobileResource.class);
			return true;
		case "CONFIGURATION":
			this.db.dropCollection(ConfigurationSection.class);
			return true;
		case "MESSAGE":
			this.db.dropCollection(Message.class);
			return true;
		case "SESSION":
			this.db.dropCollection(MobileDeviceSession.class);
			return true;
		default:
			return false;
		}
	}

	public boolean resetAll() {
		boolean done = this.resetDocumentStore("RESOURCE");
		done &= this.resetDocumentStore("CONFIGURATION");
		done &= this.resetDocumentStore("MESSAGE");
		done &= this.resetDocumentStore("SESSION");
		return done;
	}

	public void deleteMessage(String messageId) {
		Message instance = new Message();
		instance.setId(messageId);
		db.remove(instance, Message.class);
	}

	public static void main(String[] args) throws InterruptedException {
		DocumentStore store = new DocumentStore();
		// Logger.info("starting : " + store.count());
		// store.storeNewMessage(createTestMessage());
		// store.storeNewMessage(createTestMessage());
		// store.storeNewMessage(createTestMessage());
		// store.storeNewMessage(createTestMessage());
		//
		// Message next = store.findNextMessageToProcess();
		// Logger.info(next.getId() + " - "
		// + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new
		// Date(next.getTimestamp())));
		// Logger.info("processing message....");
		// store.setMessageProcessed(next.getId());
		Logger.info("done");
		Logger.info(store.listDistinctResourceType());
		//
		// List<MobileResource> list = listVehicleResources();
		//
		// store.addResourceList(list);
		// store.updateResourceList(list);

		// List<MobileResource> vehicles = store.listResourceByType("VEHICLE");
		// Logger.info("vehicle: " + vehicles.size());

		// List<MobileResource> listOspedali = listOspedaliResources();
		// store.addResourceList(listOspedali);
		// // store.updateResourceList(listOspedali);
		// List<MobileResource> ospedali = store.listResourceByType("OSPEDALI");
		// Logger.info("ospedali: " + ospedali.size());

	}

	/*
	 * @Test public final void generaListElement(){ /* MessageStore store = new
	 * MessageStore(); List<MobileResource> listOspedali = listOspedaliResources();
	 * // store.addResourceList(listOspedali);
	 * store.updateResourceList(listOspedali); List<MobileResource> ospedali =
	 * store.listResourceByType("OSPEDALI"); Logger.info("ospedali: " +
	 * ospedali.size());
	 */

	/*
	 * List<MobileResource> list = listVehicleResources();
	 * //store.addResourceList(list); store.updateResourceList(list);
	 * 
	 * List<MobileResource> vehicles = store.listResourceByType("VEHICLE");
	 * Logger.info("vehicle: " + vehicles.size());
	 * 
	 * }
	 */

	private static List<MobileResource> listVehicleResources() {
		String group = "POSTAZIONE";
		String resource = "MEZZO";
		List<MobileResource> list = new ArrayList<MobileResource>();

		for (int j = 0; j < 25; j++) {
			String g = group + " " + j;
			for (int i = 0; i < 10; i++) {
				MobileResource r = new MobileResource();
				r.setGroup(g);
				r.setType("VEHICLE");
				r.setName(resource + " " + i);
				r.setValue("" + i);
				r.setId(g + "_" + r.getType() + "_" + r.getName());
				list.add(r);
			}

		}
		return list;
		// Map<String, MobileResource> result = list.stream()
		// .collect(Collectors.toMap(MobileResource::getGroup,
		// Function.identity()));
		// return result;
	}

	private static List<MobileResource> listOspedaliResources() {
		String group = "OSPEDALE";
		String resource = "REPARTO";
		List<MobileResource> list = new ArrayList<MobileResource>();

		for (int j = 0; j < 25; j++) {
			String g = group + " " + j;
			for (int i = 0; i < 10; i++) {
				MobileResource r = new MobileResource();
				r.setGroup(g);
				r.setType("OSPEDALI");
				r.setName(resource + " " + i);
				r.setValue("" + i);
				r.setId(g + "_" + r.getType() + "_" + r.getName());
				list.add(r);
			}

		}
		return list;
	}

	private static Message createTestMessage() {
		Message m = new Message();
		m.setId(UUID.randomUUID().toString());
		m.setFrom(DeviceInfoUtil.createDeviceInfo("").getDeviceId());
		m.setPayload("{}");
		m.setPriority(5);
		m.setProcessed(false);
		m.setRpcOperation("SBS");
		m.setTimeout(30000);
		m.setTo("");
		m.setTtl(30000);
		m.setTimestamp(Calendar.getInstance().getTimeInMillis());
		m.setType("CHECKIN");
		// m.setNewField("TEST");
		return m;
	}

}
