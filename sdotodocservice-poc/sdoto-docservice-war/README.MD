**Avviamento della webapp**
Per avviare la webapp lanciare maven install del progetto padre e poi mvn tomcat7:run del war figlio Una volta vviata la webapp puntare dal browser al link http://127.0.0.1:8088/sdoto-docservice-war/rest/documentService/test e verificare che sia restituita la stringa "Got it!". 


**WADL GENERATOR**
Per generare il file WADL puntare al link http://127.0.0.1:8088/sdoto-docservice-war/rest/application.wadl.
Utilizzando SoapUI è possibile generare uno stub di un client rest per testare la webapp. in particolare bisogna creare un RestProject e cliccare su ImportWADL ed inserire la URL sopra indicata.

Sulla sinistra compariranno tutte le richieste. L'operazione va fatta dopo aver avviato la webapp.

