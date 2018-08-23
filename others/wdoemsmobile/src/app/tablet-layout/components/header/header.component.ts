import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { DeviceStatus, DeviceStatusService, AgentService, CurrentTurn, ApplicationStatus, TimeService, ApplicationStatusService, LocalBusService, ResourceListService, Event, BatteryStatus, ConnectionStatus, GpsStatus, StorageService } from '../../../service/service.module';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';
import { Subject } from 'rxjs/Subject';
import * as moment from 'moment';
import { ConfigurationService } from '../../../service/core/configuration.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';


@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush,
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
    @BlockUI('header-section') blockUI: NgBlockUI;

    pushRightClass: string = 'push-right';
    batteryClass: string = "mdi-battery";
    batteryLevel: string = "";
    //signalClass: string = "mdi-signal";
    signalClass: string = "mdi-lan-connect";
    gpsClass: string = "mdi-map-marker-radius";
    /*   gpsCoordLat: string = "0";
      gpsCoordLong: string = "0"; */
    gpsCoord: any;
    checkedIn: boolean;
    isIntervention: boolean = false;
    currentTime: Promise<Date>;
    currentTurn: CurrentTurn;
    observableCurrentTime: Observable<Date>;
    observableApplicationStatus: Observable<ApplicationStatus>;
    observableDeviceStatus: Observable<DeviceStatus>;
    appStatusSubscription: Subscription;
    routerSubscription: Subscription;
    deviceStatusSubscription: Subscription;
    deviceStatePoller;
    activeTurn: boolean;
    time_turn: number;


    constructor(private agentService: AgentService,
        private bus: LocalBusService,
        private resourceService: ResourceListService,
        private appStatusService: ApplicationStatusService,
        private timeService: TimeService,
        private translate: TranslateService,
        public router: Router,
        public deviceStatusService: DeviceStatusService,
        private configService: ConfigurationService,
        private storage: StorageService

    ) {
        this.routerSubscription = this.router.events.subscribe(val => {
            if (
                val instanceof NavigationEnd &&
                window.innerWidth <= 992 &&
                this.isToggled()
            ) {
                this.toggleSidebar();
            }
        });
    }

    ngOnInit() {
        this.activeTurn = this.configService.getConfigurationParam("GENERAL", "ACTIVE_TURN", true);
        this.time_turn = this.configService.getConfigurationParam("GENERAL", "BATTERY_PERCENT", '5');
        this.currentTime = this.timeService.getAsyncCurrentTime();
        this.observableCurrentTime = new Observable<Date>((observer: Subscriber<Date>) => {

            /**/
            setInterval(() => {
                /*this.logger.info('setInterval  from  ' + (observer));*/
                this.timeService.getAsyncCurrentTime().then(
                    (d) => {
                        observer.next(d);
                    },
                    (err) => {
                        observer.next(err);
                    }
                );
            }, 1000);

        });
        this.observableApplicationStatus = this.appStatusService.getApplicationStatusObservable();
        this.observableDeviceStatus = this.deviceStatusService.getDeviceStatusObservable();
        let status = this.appStatusService.getCurrentStatus();
        this.initListener();
        if (status) {
            if (status.currentEvent && this.appStatusService.isFirstLoad()) {
                let turn = status.currentTurn;
                if (turn && turn.resource) {
                    this.appStatusService.updateFirstLoad(false);
                    this.bus.notifyAll('reloadIntervention', {});
                }
            }
        }

        this.appStatusService.requestApplicationStatus();
        this.deviceStatePoller = setInterval(() => {
            this.agentService.requestDeviceState();
        }, 5000);

    }


    initListener() {
        this.appStatusSubscription = this.observableApplicationStatus.subscribe((val: ApplicationStatus) => {


            this.currentTurn = val.currentTurn;
            this.checkedIn = (val.currentTurn != undefined && val.currentTurn != null);
            this.isIntervention = val.currentEvent != undefined;
            if (this.currentTurn && !this.currentTurn.endturnDate) {
                this.calcEndTurnDate(this.currentTurn);
            }
        });

        this.deviceStatusSubscription = this.observableDeviceStatus.subscribe((deviceStatus: DeviceStatus) => {
            ////console.log("device status update!!");
            this.updateBatteryClass(deviceStatus.batteryStatus);
            this.updateConnectionClass(deviceStatus.connectionStatus);
            this.updateGpsClass(deviceStatus.gpsStatus);
            this.updateCurrentPosition(deviceStatus.gpsCoord)
        });
    }

    updateCurrentPosition(coord) {
        this.gpsCoord = coord;
    }

    requestLastPosition() {
        let position = this.deviceStatusService.getLastPosition();
        this.gpsCoord = position
    }


    ngOnDestroy() {
        clearInterval(this.deviceStatePoller);
        this.appStatusSubscription.unsubscribe();
        this.routerSubscription.unsubscribe();
        this.deviceStatusSubscription.unsubscribe();

    }

    isToggled(): boolean {
        const dom: Element = document.querySelector('body');
        return dom.classList.contains(this.pushRightClass);
    }

    toggleSidebar() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle(this.pushRightClass);
    }

    rltAndLtr() {
        const dom: any = document.querySelector('body');
        dom.classList.toggle('rtl');
    }

    logout() {
        this.storage.removeItem('isLoggedin');
        this.router.navigate(["login"]);
    }

    changeLang(language: string) {
        this.translate.use(language);
    }

    location() {

    }

    callback() {
        /*   let msg: string = '{"id":null,"from":null,"identity":null,"to":null,"payload":"{\"luogo\":\"TVZ\",\"patologia\":\"TST\",\"criticita\":\"B\",\"criticitaObject\":{\"detailMap\":{\"\":\"YYYYYYYYYYY\"},\"detailArray\":[{\"label\":\"\",\"value\":\"YYYYYYYYYYY\"}]},\"modAttivazione\":\"Avanzato BLU\",\"sirena\":\"NO\",\"comune\":\"BO\",\"localita\":\"Galvani\",\"localitaObject\":{\"detailMap\":{\"\":\"YYYYYYYYYYY\"},\"detailArray\":[{\"label\":\"\",\"value\":\"YYYYYYYYYYY\"}]},\"cap\":\"40131\",\"indirizzo\":\"Via Cristoforo Colombo\",\"indirizzoObject\":{\"detailMap\":{\"Luogo\":\"ZZZZZZZZ\",\"Via\":\"XXXXXXXXX\"},\"detailArray\":[{\"label\":\"Luogo\",\"value\":\"ZZZZZZZZ\"},{\"label\":\"Via\",\"value\":\"XXXXXXXXX\"}]},\"luogoPubblico\":\"Prova Luogo Pubblico\",\"luogoPubblicoObject\":{\"detailMap\":{\"\":\"xxxxxxxxxxxxxxxxx x  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\"},\"detailArray\":[{\"label\":\"\",\"value\":\"xxxxxxxxxxxxxxxxx x  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\"}]},\"civico\":\"110\",\"piano\":\"3\",\"codEmergenza\":\"1234AB34TR\",\"codEmergenzaObject\":{\"detailMap\":{\"\":\"YYYYYYYYYYY\"},\"detailArray\":[{\"label\":\"\",\"value\":\"YYYYYYYYYYY\"}]},\"dataOraSegnalazione\":\"10/02/2017 - 13:50\",\"dataOraSegnalazioneObject\":{\"detailMap\":{\"Data\":\"XXXXXXXXX\",\"Ora\":\"ZZZZZZZZ\"},\"detailArray\":[{\"label\":\"Data\",\"value\":\"XXXXXXXXX\"},{\"label\":\"Ora\",\"value\":\"ZZZZZZZZ\"}]},\"personaRif\":\"Gianluigi Albertazzi\",\"telefono\":\"3453456789\",\"nPazienti\":\"1\",\"sex\":\"M\",\"eta\":\"34 anni\",\"note\":\"PROVA NOTE  kkkkkkkkkkkkkkkkkk  kkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkk kkkkkkkkkkkkkk\",\"noteObject\":{\"detailMap\":{\"\":\"PROVA NOTE  kkkkkkkkkkkkkkkkkk  kkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkk kkkkkkkkkkkkkkxxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 \"},\"detailArray\":[{\"label\":\"\",\"value\":\"PROVA NOTE  kkkkkkkkkkkkkkkkkk  kkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkk kkkkkkkkkkkkkkxxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 \"}]}}","timestamp":0,"timeout":0,"fromUrl":null,"type":"ACT","rpcOperation":null,"relatesTo":null,"processed":false,"ttl":0,"priority":0}';
          this.agentService.callback(msg).subscribe((e) => {
          }); */
    }

    showCheckIn(route) {
        if (this.checkedIn) {
            return;
        }
        this.bus.notifyAll("masks", { type: 'mask' });
        this.resourceService.requestParkingList().then(
            (success) => {

                const event: Event = {

                    type: 'parkingList', payload: {
                        items: success
                    }
                }

                this.bus.notifyAll('parkingList', event);
                this.bus.notifyAll("masks", { type: 'unmask' });
                this.navigateTo(route);

            },
            (error) => {

            });

    }

    endTurn() {
        let s = this.bus.addObserver('confirmDialog').subscribe((event) => {
            if (event.type === 'confirmDialogResponse') {
                this.bus.removeAllObservers('confirmDialog', [s]);
                if (event.payload) {
                    this.doCheckOut();
                }
            }
        });
        this.bus.notifyAll('dialogs', { type: 'confirmDialog', payload: { width: "500px", title: "Sei sicuro?", content: "Procedere con la fine del turno?" } });

    }

    private doCheckOut() {
        /*this.bus.notifyAll("masks", { type: 'mask' });*/
        this.blockUI.start();
        setTimeout(() => {
            this.appStatusService.clearCurrentTurn();
            /*this.bus.notifyAll("masks", { type: 'unmask' });*/
            this.blockUI.stop();
        }, 1500);


    }


    navigateTo(route) {

        this.router.navigate([route]);
    }

    public calcEndTurnDate(currentTurn: CurrentTurn) {
        if (!currentTurn) {
            return;
        }
        let start = currentTurn.startTurnDate;
        let now = moment(start);
        let end = now.add(currentTurn.duration, "hour").toDate();
        this.appStatusService.updateEndTurnDate(end);

    }

    public updateConnectionClass(connectionStatus: ConnectionStatus) {
        /*this.signalClass = "mdi-signal";
        this.signalClass += connectionStatus.connected ? "" : "-off";*/
        this.signalClass = "mdi-lan-connect";
        if (!connectionStatus.connected) {
            this.signalClass = "mdi-lan-disconnect";
        }



    }

    public updateGpsClass(gpsStatus: GpsStatus) {
        let status: number = gpsStatus.gpsStatus;
        if (status <= 0) {
            this.gpsClass = "mdi-map-marker-off";
        }
        else {
            this.gpsClass = "mdi-map-marker-radius";
        }
        /*      switch (status) {
                 //STATUS_TEMP_UNAVAILABLE = 0;
                 case 0: {
                     this.gpsClass = "mdi-map-marker-off";
                     break;
                 }
                 //STATUS_OUT_OF_ORDER = -1;
                 case -1: {
                     this.gpsClass = "mdi-map-marker-off";
                     break;
                 }
                 case 1: {
                     this.gpsClass = "mdi-map-marker-radius";
                     break;
                 }
                 //STATUS_AVAILABLE = 1;
                 default: {
                     this.gpsClass = "mdi-map-marker-off";
                     break;
                 }
             } */
    }

    public updateBatteryClass(batteryStatus: BatteryStatus) {
        this.batteryClass = "mdi-battery";



        if (batteryStatus.lifePercent != undefined && batteryStatus.lifePercent != null &&
            batteryStatus.acConnected != undefined && batteryStatus.acConnected != null) {
            let connected = batteryStatus.acConnected;
            let level = Number(batteryStatus.lifePercent);
            this.batteryLevel = String(level) + "%";

            if (level <= this.time_turn) {
                this.batteryClass += connected ? "-charging" : "-outline";
            } else {

                switch (level) {
                    case 0: {
                        this.batteryClass += connected ? "-charging" : "-outline";
                        break;
                    }
                    case 50: {
                        this.batteryClass += connected ? "-charging-40" : "-" + level;
                        break;
                    }
                    case 100: {
                        this.batteryClass += connected ? "-charging-" + level : "";
                        break;
                    }
                    default: {
                        if (level > 0 && level <= 30) {
                            level = 20;
                        } else if (level > 30 && level < 50) {
                            level = 40;
                        } else if (level > 50 && level <= 80) {
                            level = 60;
                        } else if (level > 80 && level < 100) {
                            level = 80;
                        } else {
                            level = null;
                        }

                        this.batteryClass += connected ? "-charging" : "";
                        this.batteryClass += level == null ? "" : "-" + level;
                    }
                }
            }
        }



    }

}
