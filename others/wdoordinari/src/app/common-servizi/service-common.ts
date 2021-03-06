import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';

import { Observable, BehaviorSubject, Subject } from 'rxjs/Rx';
//import { BackendConfigService } from '../service/config.service';
//import 'rxjs/Rx';


import {
    TransportModuleServiceService, CheckResultDTO
} from '../../services/services.module'
import { ComponentHolderService } from 'app/service/shared-service';
import { ArchiviaServizioConfirmModal } from 'app/common-servizi/archivia-servizio.modal';
import { Router } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Injectable()
export class ServiceCommon {

    constructor(
        private componentService: ComponentHolderService,
        private transportService: TransportModuleServiceService,
        private router: Router,
        private modalService: NgbModal,
    ) { }


    public async checkAndArchiveService(serviceIdSelected: string,serviceCode:string, managedFlag: boolean): Promise<boolean> {
        if (serviceIdSelected == null || serviceIdSelected.length == 0)
            return false;

        let undispatchFlag = await this.checkUndispatchService(serviceIdSelected);
        console.log(undispatchFlag);
        if (undispatchFlag.ok == true) { //nessun rilevamento tappa - chiedo se archiviare
            this.componentService.getRootComponent().unmask();
            try {
                await this.componentService.getRootComponent().showConfirmDialog('Attenzione!', "Nessuna tappa del servizio possiede un rilevamento orario.\nContinuare con l'archiviazione?");
                let toRet = await this.archive(serviceIdSelected, serviceCode, undispatchFlag.ok, managedFlag);
                return toRet;
            } catch (reason) {
                console.log("Archiviazione non effettuata " + reason);
                this.componentService.getRootComponent().unmask();
            }

        } else { //C'è rilevamento tappa --> avanzamento automatico archiviazione
            return await this.archive(serviceIdSelected,serviceCode, undispatchFlag.ok, managedFlag);
        }
    }

    private async archive(serviceIdSelected: string, serviceCode: string, undispatchFlag: boolean, managedFlag: boolean): Promise<boolean> {
        let nonSSNPaid = false;
        this.componentService.getRootComponent().mask();
        //il valore nonSSNPaid è il check selezionato da utente
        let nonSSNPaidBookingPresent = await this.checkNonSSNBookingPaidPresent(serviceIdSelected);
        //try {
        this.componentService.getRootComponent().unmask();
        //this.componentService.getRootComponent().showConfirmDialog('Attenzione!', "E' possibile archiviare il servizio. Proseguire ?").then((resultArchive) => {
        let modal = this.modalService.open(ArchiviaServizioConfirmModal, { size: 'lg' });
        modal.componentInstance.serviceCode = serviceCode;
        modal.componentInstance.paidEnabled = nonSSNPaidBookingPresent.ok;
        try {
            let result = await modal.result;
            if (nonSSNPaidBookingPresent)
                nonSSNPaid = result;
            let res = await this.archiveService(serviceIdSelected, undispatchFlag, nonSSNPaid, managedFlag);
            return res;
        } catch (reason) {
            console.log("Archiviazione non effettuata " + reason);
            this.componentService.getRootComponent().unmask();
            return false;
        }
    }

    public async archiveService(serviceIdSelected: string, undispatchFlag: boolean, bookingNonSSNPaidFlag: boolean, managedFlag: boolean): Promise<boolean> {
        this.componentService.getRootComponent().mask();
        let model = {
            serviceId: serviceIdSelected,
            checkServiceDispatchFlag: undispatchFlag,
            bookingNonSSNPaid: bookingNonSSNPaidFlag,
            managedFlag: managedFlag
        };

        try {
            let result = await this.transportService.archiveService(model).toPromise();
            this.componentService.getRootComponent().showToastMessage('success', 'Servizio archiviato');
            this.componentService.getRootComponent().unmask();
            if(result.result && result.result!=null){
                return true;
            }else{
                return false;
            }
        } catch (err) {
            this.componentService.getRootComponent().unmask();
            return false;
        }

    }

    public async checkIsManaged(serviceId: string): Promise<CheckResultDTO> {
        return this.transportService.checkIsManaged(serviceId).toPromise();
    }

    public async checkUndispatchService(serviceId: string): Promise<CheckResultDTO> {
        return this.transportService.checkUndispatchedService(serviceId).toPromise();
    }

    public async checkNonSSNBookingPaidPresent(serviceId: string): Promise<CheckResultDTO> {
        return this.transportService.checkNonSSNBookingPaid(serviceId).toPromise();
    }
}
