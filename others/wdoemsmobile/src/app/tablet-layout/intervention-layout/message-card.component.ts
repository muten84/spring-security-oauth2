import { Component, } from '@angular/core';
import { slideToBottom } from '../../router.animations';
import { ConfigurationService } from '../../service/core/configuration.service';
import { AgentService, AckCommand, ApplicationStatusService } from '../../service/service.module';
import { Router } from '@angular/router';

/**
 * @title Card with multiple sections
 */
@Component({
    selector: 'app-message-card',
    templateUrl: 'message-card.component.html',
    styleUrls: ['message-card.component.scss'],
    animations: [slideToBottom()]
})
export class MessageCardComponent {

    disableAnimation;
    titleMsg;
    bodyMsg;
    eventPresent: boolean = true;

    constructor(private configService: ConfigurationService,
        private agent: AgentService,
        private appStatus: ApplicationStatusService,
        private router: Router) {

    }

    ngOnInit() {
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.disableAnimation = !enableAnimation;
        if (!this.appStatus.getCurrentEvent()) {
            this.titleMsg = "Chiusura intervento da Centrale";
            this.bodyMsg = "Attenzione l'intervento è stato chiuso dalla centrale. Contattare la centrale."
            this.eventPresent = false;
            this.bodyMsg = (<string>this.bodyMsg).toUpperCase();

        }
        else if (this.appStatus.getCurrentEvent().notifyUpdate) {
            let updateLabel;
            this.titleMsg = "Aggiornamento Dati Intervento";
            this.bodyMsg = "Dalla Centrale Operativa risultano modificati i seguenti dati: ";
            updateLabel = this.appStatus.getCurrentEvent().listLabelUpdate;
            this.bodyMsg += updateLabel;
            this.bodyMsg = (<string>this.bodyMsg).toUpperCase();

            /*  let length = this.appStatus.getCurrentEvent().listLabelUpdate.length;
             for (var i = 0; i < length; i++) {
                 updateLabel += this.appStatus.getCurrentEvent().listLabelUpdate[i] + (i == (length - 1) ? "" : ", ");
             }
             this.bodyMsg += updateLabel; */
            this.eventPresent = true;
        } else {
            this.titleMsg = "Nuovo Intervento";
            this.bodyMsg = "Conferma nuova attivazione inviata dalla Centrale Operativa";
            this.eventPresent = true;
            this.bodyMsg = (<string>this.bodyMsg).toUpperCase();
        }

        //inviare messaggio per attivazione ricevuta
        this.sendAckMessage();

    }

    sendAckMessage() {
        let last = this.appStatus.getLastProcessedMessage();
        const ackCommand: AckCommand = {
            relatesTo: last.id,
            relatesToType: last.type,
            result: true
        }
        this.agent.sendAsynchMessage("ACK", ackCommand).then(
            (success) => {
                //console.log("act messagge send success: " + success);
            },
            (failure) => {
                //console.log("act messagge send failure: " + failure);
            }
        )
    }

    public dispatchCard() {
        if (this.eventPresent) {
            this.router.navigate(['/intervention/newIntervention']);
        }
        else {
            this.router.navigate(['/dump118']);
        }
    }

}
