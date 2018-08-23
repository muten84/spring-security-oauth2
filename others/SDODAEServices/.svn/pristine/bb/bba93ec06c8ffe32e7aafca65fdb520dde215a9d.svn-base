package it.eng.areas.ems.sdodaeservices.entity;

public enum ParametersEnum {
	ENABLE_PRIVACY("Abilita nuove disposizioni sulla privacy"), //
	REGEXP_PWD("Regular expression per verifica correttezza password"), //
	REGEXP_PWD_DOUBLE_CHAR_MAX(
			"Regular expression per verifica correttezza password con massimo 2 caratteri identici consecutivi"), //
	REGEXP_PWD_FOUR_CHAR("Regular expression per verifica correttezza password con almeno 4 caratteri alfabetici"), //
	HISTORY_PWD_SIZE("Dimensione delle ultima password salvate"), //
	ENABLE_POSTGIS_SAVE("Consente di abilitare la gestione dei DAE anche su PostGIS"), //
	ADMIN_MAIL("Mail degli amministratori separate da  ;"), //
	GPS_COORD_MINUTES_EXPIRATION("Consente di definire per quanti minuti vengono ritenute valide le coordinate GPS"), //
	ENABLE_MAIL("Abilita la notifica via mail"), //
	USE_PROXY("Abilita il proxy"), //
	REVERSE_GEOCODING_SERVICE("URL del servizio SOAP di Reverse Geocoding"), //
	PUSH_NOTIFICATION_ENABLED("Abilita il sistema di notifiche PUSH"), //
	PROXY_ADDRESS("Indirizzo del proxy"), //
	PROXY_PORT("Porta del Proxy"), //
	PROXY_USERNAME("Username utilizzata per autenticazione proxy"), //
	PROXY_PASSWORD("Password utilizzata per autenticazione proxy"), //
	PUSHWOOSH_URL("URL Pushwoosh"), //
	PUSH_APPLICATION("Codice applicazione di PushWoosh"), //
	PUSH_AUTH("Codice autenticazione di Push Woosh"), //
	DISTANCE_THRESHOLD("Distanza minima per l'allertamento dei First Responder"), //
	ENABLE_POSTGIS("Abilita il salvataggio sul server Postgis"), //
	PUBLIC_ADDRESS("Indirizzo pubblico usato per generare gli URL nelle mail "), //
	TIMEOUT_RESET_PASSWORD("Tempo in ore di validità della richiesta di reset della password"), //
	REGEX_CODICE_FISCALE("Espressione regolare per controllare il codice fiscale"), //
	TOKEN_EXPIRED_TIMEOUT("Timeout dopo il quale i token non utilizzati verranno eliminati (min)"), //
	REGION_MASTER_EMAIL("Email del Region master"), //
	DATE_MAIL_ALERT_PERIODICO("Date in cui vengono inviate le mail di alert periodico, formato gg/mm separate da ;"), //
	MAX_ACTIVED_FIRST_RESPONDER("Il numero massimo di First Responder che possono accettare un'emergenza"), //
	PRIVACY_AGREEMENT_TEXT(
			"Indica il disclaimer da visualizzare quando viene avviata per la prima volta l'applicazione"), //
	NEW_EVENT_MESSAGE_TEXT("Indica il messaggio visualizzato nella notifica quando viene creato un nuovo evento"), //
	UPDATE_EVENT_MESSAGE_TEXT("Indica il messaggio visualizzato nella notifica quando viene aggiornato un evento"), //
	CLOSE_EVENT_MESSAGE_TEXT("Indica il messaggio visualizzato nella notifica quando viene chiuso un evento"), //
	ABORT_EVENT_MESSAGE_TEXT("Indica il messaggio visualizzato nella notifica quando viene chiuso un evento"), //

	ENABLE_NOTIFICATION_ALGORITHM("Abilita l'algoritmo per la selezione degli eventi in base alle notifiche ricevute"), //

	UPDATE_PASSWORD_DAYS("Giorni di validità della password"), //
	SAVE_INTERVENTION_COORD(
			"Consente di abilitare il salvataggio delle coordinate di un FR inviate durante le fasi dell'intervento"), //
	HOUR_DELETE_FR("Scadenza in ore dei FR da cancellare"), //
	MUNICIPALITY_EQUAL("Se messo a true seleziona i FR per comune di cometenza usando l'equal sul comune");

	private String description;

	private ParametersEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
