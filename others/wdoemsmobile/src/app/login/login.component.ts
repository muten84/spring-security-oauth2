import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { routerTransition } from '../router.animations';
import { AgentService, ApplicationStatusService, LocalBusService, Logger, StorageService } from '../service/service.module';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    animations: [routerTransition()]
})
export class LoginComponent implements OnInit {

    @BlockUI('login-section') blockUI: NgBlockUI;

    constructor(private logger: Logger,
        public router: Router,
        private appStatusService: ApplicationStatusService,
        private agent: AgentService,
        private bus: LocalBusService,
        private storage: StorageService) { }

    value: string;
    position = 'tl';
    nativeValue: string = '';
    disabled = false;
    open = false;
    operativeArea: string = 'Nessuna Territorio Selezionato';
    selected = false;
    coList: Array<any> = [];

    ngAfterViewInit() {
        // this.logger.info(window)
    }

    ngOnInit() {
        this.coList = [
            { name: 'BOLOGNA' },
            { name: 'FERRARA' },
            { name: 'MODENA' },
            { name: 'PARMA' },
            { name: 'PIACENZA' },
            { name: 'REGGIO EMILIA' },
            { name: "RAVENNA" },
            { name: 'RIMINI' },
            { name: "FORLI' CESENA" }]
    }

    onLoggedin() {
        // localStorage.setItem('isLoggedin', 'true');
        this.mask();
        this.selected = false;
        this.agent.getSiteInfo(this.operativeArea).then(
            (response: any) => {
                this.logger.info("site info procedure completed for: " + JSON.stringify(response));
                this.selected = true;
                this.appStatusService.updateCurrentSiteInfo(response);
                this.storage.saveToLocal("siteInfo", response);
                this.unmask();
                //TODO: invoke router navigate [routerLink]="['/loader']"
                this.router.navigate(["/loader"]);


            },
            (error: any) => {
                this.logger.info("error when selecting operative area");
                this.selected = true;
                this.unmask();
                const msg = { type: 'messageDialog', payload: { width: '500px', title: "Attenzione", content: "Non e' stato possibile effettuare il legame con la centrale. Contattare l'assistenza." } };
                this.bus.notifyAll('dialogs', msg);
            });
    }

    private mask() {
        this.blockUI.start();
    }

    private unmask() {
        this.blockUI.stop();
    }

    operativeAreaSelected(area) {
        this.logger.info("operativeAreaSelected:" + area);
        this.selected = true;
        this.operativeArea = area.name;

    }

    select(choice) {
        this.logger.info("choice is: " + choice)
        this.operativeArea = choice;
    }

    get pTop() {
        return (this.position === 'tl' || this.position === 'tr') ? 0 : null;
    }

    get pBottom() {
        return (this.position === 'bl' || this.position === 'br') ? 0 : null;
    }

    get pLeft() {
        return (this.position === 'tl' || this.position === 'bl') ? 0 : null;
    }

    get pRight() {
        return (this.position === 'tr' || this.position === 'br') ? 0 : null;
    }
}
