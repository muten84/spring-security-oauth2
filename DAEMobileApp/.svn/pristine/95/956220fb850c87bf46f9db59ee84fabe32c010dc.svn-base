Cordova CLI: 6.3.1
Gulp version:  CLI version 1.2.1
Gulp local:   Local version 3.9.1
Ionic Framework Version: 1.3.1
Ionic CLI Version: 2.0.0
Ionic App Lib Version: 2.0.0
ios-deploy version: 1.8.6 
ios-sim version: 5.0.8 
OS: Mac OS X El Capitan
Node Version: v4.4.2
Xcode version: Xcode 8.0 Build version 8A218a


ionic plugin add cordova-plugin-statusbar
ionic plugin add cordova-plugin-contacts
ionic plugin add cordova-plugin-camera
ionic plugin add cordova-plugin-vibration
ionic plugin add cordova-plugin-device
ionic plugin add cordova-plugin-console
ionic plugin add cordova-plugin-transport-security
ionic plugin add cordova-plugin-inappbrowser
ionic plugin add cordova-plugin-splashscreen
ionic plugin add cordova-plugin-admobpro

ionic plugin add cordova-plugin-x-socialsharing
ionic plugin add cordova-plugin-email-composer
ionic plugin add ionic-plugin-keyboard
ionic plugin add cordova-sms-plugin
ionic plugin add https://github.com/apache/cordova-plugin-whitelist.git
ionic plugin add https://github.com/devgeeks/Canvas2ImagePlugin.git
ionic plugin add https://github.com/wymsee/cordova-imagePicker.git
ionic plugin add https://github.com/litehelpers/Cordova-sqlite-storage.git#0.7.14
ionic plugin add https://github.com/EddyVerbruggen/Flashlight-PhoneGap-Plugin
ionic plugin add https://github.com/katzer/cordova-plugin-local-notifications


#Tool da utilizzare

npm, bower, gulp, ionic.

#Comandi utili 

`npm install` (per le dipendenze di node)

`bower install` (per le dipendenze del client)

`ionic serve -nb  --address localhost` per avviare il servizio di ionic per testare l'app sul browser

[http://localhost:8100/](http://localhost:8100/)

`open -a Google\ Chrome --args --disable-web-security --user-data-dir` per aprire chrome disattivando il cross origin

#Configurazioni 
`bower.json`: una volta installata una nuova dipendenza per il client, va impostata in questo file

#Deploy
Si utilizza l'applicativo: [https://www.npmjs.com/package/mobile-app-distribution](https://www.npmjs.com/package/mobile-app-distribution)

Per installare: `npm i -g mobile-app-distribution`

Ho creato un ftp sulla macchina 10.118.32.61 che punta direttamente alla cartella della webapp sul tomcat DAETest.

##Preparazione
Per pubblicare il file firmato col certificato enterprise (sia IPA che APK):
Creare la cartella wd che conterrà la pagina usata per scaricare IPA e APK:
`distribute wd`

Creata la cartella spostarla sul server web, in modo che sia accessibile dall'esterno.



##IOS

Per effettuare il build con firma:
``
Per fare la build e generare lo xarchive
``




##Android

Per effettuare il build con firma:
``
