/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.rest.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * @author Bifulco Luigi
 *
 */

@RestController //
@RequestMapping("/api/rest/message")
@Api(value = "MessageController", protocols = "http")
public class MessageControllerImpl {

	// @Autowired
	// private MessageRepository messageRepository;

	// @Autowired
	// private MessageListRepository messageQueueRepository;

	// @Autowired
	// private RestTemplate restTemplate;
	//
	// @Value("${rpc.service.url}")
	// private String rpcServiceUrl;
	//
	// @RequestMapping(path = "/sendMessage", //
	// method = RequestMethod.POST, //
	// consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ApiOperation(value = "sendMessage", notes = "Permette di inviare e gestire
	// nelllo stesso thread un messaggio")
	// @ApiResponses(value = { //
	// @ApiResponse(code = 200, message = "OK"), //
	// @ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	// public @ResponseBody Message sendMessage(@RequestBody Message message,
	// HttpServletRequest httpRequest) {
	// String ipAddress = httpRequest.getRemoteAddr();
	// // message.setIpAddress(ipAddress);
	//
	// // messageRepository.save(message);
	// messageQueueRepository.pushMessage(message.getFrom(), message);
	// return message;
	// }
	//
	// @RequestMapping(path = "/receiveMessage/{queueName}", //
	// method = RequestMethod.GET, //
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ApiOperation(value = "receiveMessage", notes = "Scoda un messaggio dalla
	// coda")
	// @ApiResponses(value = { //
	// @ApiResponse(code = 200, message = "OK"), //
	// @ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	// public Message receiveMessage(@PathVariable("queueName") String queueName) {
	// return messageQueueRepository.popMessage(queueName);
	// }
	//
	// @RequestMapping(path = "/count/{queueName}", //
	// method = RequestMethod.GET, //
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ApiOperation(value = "count", notes = "Permette di contare quanti messaggi
	// ci sono in coda")
	// @ApiResponses(value = { //
	// @ApiResponse(code = 200, message = "OK"), //
	// @ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	// public String countMessages(@PathVariable("queueName") String queueName) {
	// // return "" + messageRepository.count();
	// return "" + messageQueueRepository.countMessages(queueName);
	// }
	//
	// @RequestMapping(path = "/rpc", //
	// method = RequestMethod.POST, //
	// consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ApiOperation(value = "rpc", notes = "Permette di inviare e gestire nello
	// stesso thread un messaggio")
	// @ApiResponses(value = { //
	// @ApiResponse(code = 200, message = "OK"), //
	// @ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	// public @ResponseBody Message rpc(@RequestBody Message message,
	// HttpServletRequest httpRequest) {
	// String url = this.rpcServiceUrl + "/mobile/rpc";
	// //return restTemplate.postForObject(url, message, Message.class);
	// //TODO test only!!!!
	// Message m = fromMessage(message);
	// m.setPayload("{\"result\": true }");
	// m.setProcessed(true);
	// return m;
	//
	// }
	//
	// @RequestMapping(path = "/resource/{type}", //
	// method = RequestMethod.GET, //
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ApiOperation(value = "resource", notes = "Permette di inviare e gestire
	// nello stesso thread un messaggio")
	// @ApiResponses(value = { //
	// @ApiResponse(code = 200, message = "OK"), //
	// @ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	// public @ResponseBody List<MobileResource>
	// extractMobileResourceByType(@PathVariable("type") String type) {
	// switch(type){
	// case "VEHICLE":
	// return listVehicleResources();
	// case "OSPEDALI":
	// return listOspedaliResources();
	// }
	// return null;
	// }
	//
	// private Message fromMessage(Message m){
	// Message newM = new Message();
	// newM.setFrom(m.getTo());
	// newM.setTo(m.getFrom());
	// newM.setTimestamp(Calendar.getInstance().getTimeInMillis());
	// newM.setRelatesTo(m.getId());
	// newM.setType(m.getType());
	// newM.setRpcOperation(m.getRpcOperation());
	// newM.setId(UUID.randomUUID().toString());
	// return newM;
	// }
	//
	// private static List<MobileResource> listVehicleResources() {
	// String group = "POSTAZIONE";
	// String resource = "MEZZO";
	// List<MobileResource> list = new ArrayList<MobileResource>();
	//
	// for (int j = 0; j < 25; j++) {
	// String g = group + " " + j;
	// for (int i = 0; i < 10; i++) {
	// MobileResource r = new MobileResource();
	// r.setGroup(g);
	// r.setType("VEHICLE");
	// r.setName(resource + " " + i);
	// r.setValue("" + i);
	// r.setId(g + "_" + r.getType() + "_" + r.getName());
	// list.add(r);
	// }
	//
	// }
	// return list;
	// // Map<String, MobileResource> result = list.stream()
	// // .collect(Collectors.toMap(MobileResource::getGroup,
	// // Function.identity()));
	// // return result;
	// }
	//
	// private static List<MobileResource> listOspedaliResources() {
	// String group = "OSPEDALE";
	// String resource = "REPARTO";
	// List<MobileResource> list = new ArrayList<MobileResource>();
	//
	// for (int j = 0; j < 25; j++) {
	// String g = group + " " + j;
	// for (int i = 0; i < 10; i++) {
	// MobileResource r = new MobileResource();
	// r.setGroup(g);
	// r.setType("OSPEDALI");
	// r.setName(resource + " " + i);
	// r.setValue("" + i);
	// r.setId(g + "_" + r.getType() + "_" + r.getName());
	// list.add(r);
	// }
	//
	// }
	// return list;
	// }

}
