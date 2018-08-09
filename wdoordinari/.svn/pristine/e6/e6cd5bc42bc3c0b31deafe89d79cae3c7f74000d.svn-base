import {
    ViewChild, ElementRef, Directive,
    Component, OnInit, ViewEncapsulation,
    ViewContainerRef, EventEmitter, Input, Output,
    AfterViewInit
} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { BannerModel, BannerDataGroup } from '../core/core.module';
import { BookingModuleServiceService, BookingDTO, PatientBookingDTO, Value } from '../../services/services.module';
import { ComponentHolderService, HeaderComponent } from '../service/shared-service';
import { defIfEmpty, formatDate } from '../util/convert';
import { StaticDataService } from '../static-data/cached-static-data';
import * as _ from 'lodash';
import { calcolaTrasportato, decodeCompact2Sd, decodePhaseForDate } from 'app/util/sinottico-utils';

@Component({
    selector: 'modifica-prenotazione-header',
    templateUrl: './modifica-prenotazione-header.component.html',
    encapsulation: ViewEncapsulation.None,
})
export class ModificaPrenotazioneHeaderComponent implements OnInit, HeaderComponent, AfterViewInit {

    bookingBanner: BannerModel;
    private serviceCode: string;
    private serviceId: string;
    //private source: string;

    @Input() currentBookingId: string;

    constructor(
        private sdSvc: StaticDataService,
        private componentService: ComponentHolderService,
        private route: ActivatedRoute,
        private router: Router,
        private bookingService: BookingModuleServiceService) {

    }

    fetchHeaderData(id: string) {
        this.refreshBooking();
    }

    ngOnInit(): void {
        // this.currentId = this.route.snapshot.params['id'];
        /*this.currentBookingId = '12345678';*/
        let queryParam = this.route.snapshot.queryParams;
        console.debug('QueryParam:' + queryParam);
        if (queryParam) {
            if (queryParam.serviceCode) {
                this.serviceCode = queryParam.serviceCode;
            } else {
                console.warn('Valore del serviceCode non presente con QueryParam non nullo');
            }
            if (queryParam.serviceId) {
                this.serviceId = queryParam.serviceId;
            } else {
                console.warn('Valore del serviceId non presente con QueryParam non nullo');
            }
        }
        /*let sourceParam = this.source = this.route.snapshot.params['source'];
        if(sourceParam){
            this.source = sourceParam;
        }*/
        this.refreshBooking();
    }

    ngAfterViewInit(): void {
        this.componentService.setHeaderComponent('headerComponent', this);
    }

    getHeaderFilter(): any { };

    resetFilter(): void { };

    refreshBooking() {
        let body = [];

        //        this.bookingService.get(body).subscribe(( b ) => {
        //            this.fromItemToBannerModel( b );
        //        } );

        this.fromItemToBannerModel();
    }

    /*defIfEmpty( val ) {
        if ( val ) {
            return val;
        }
        return "n.d.";
    }*/

    fromItemToBannerModel(item?: BookingDTO): void {

        this.bookingBanner = new BannerModel();
        if (item) {
            if (item.code) {
                this.bookingBanner.headerTitle = '<span style="color: #b4bcc2;margin-bottom: 1px">Prenotazione </span>' + '<span>' + item.code + '</span>';
                //add booking webCode
                if (item.webCode) {
                    this.bookingBanner.headerTitle += '<span style="margin-left:1em; color: #b4bcc2;margin-bottom: 1px">Codice Web </span>' + '<span>' + item.webCode + '</span>';
                }
                //add booking status info
                let statusDesc = decodeCompact2Sd("BOOKING_STATUS", item.status);
                if(item.returnReady) statusDesc += " (CODA RITORNI)";
                if(item.source && item.source=='S') statusDesc += " (Archivio Storico)";
                this.bookingBanner.headerTitle += '<span style="margin-left:1em; color: #b4bcc2;margin-bottom: 1px"> Stato </span>' + '<span>' + statusDesc + '</span>';
                if(item.returnReady){
                    this.bookingBanner.headerTitle += '<span style="margin-left:0.2em" > <a href="./app#/coda-ritorni" title="Ritorna alla coda ritorni" alt="Ritorna alla coda ritorni" class="btn btn btn-outline-primary"><i class="fa fa-share-square-o" aria-hidden="true"></i></a></span>';
                }
                if(item.bookingId && !item.returnReady){
                    this.bookingBanner.headerTitle += '<span style="margin-left:0.2em" > - PRENOTAZ. DI RITORNO</span>';
                }
                if(item.returnDate ){
                    this.bookingBanner.headerTitle += '<span style="margin-left:0.2em" > - PREVEDE RITORNO</span>';
                }if (this.serviceCode) {
                    //add service code info
                    this.bookingBanner.headerTitle += '<span style="margin-left:1em; color: #b4bcc2;margin-bottom: 1px"> Servizio </span>' + '<span>' + this.serviceCode + '</span>';
                    //add return link to service detail             
                    this.bookingBanner.headerTitle += '<span style="margin-left:0.2em" > <a href="./app#/gestione-servizi/'+(item.source=='S'?'S/':'') + this.serviceId + '" title="Vai al servizio connesso" alt="Ritorna al servizio ' + this.serviceCode + ' connesso" class="btn btn btn-outline-primary"><i class="fa fa-share-square-o" aria-hidden="true"></i></a></span>';
                }
            } else {
                if (item.duplicable) {
                    this.bookingBanner.headerTitle = 'Prenotazione Duplicata';
                    this.bookingBanner.subheadings = [];
                    this.bookingBanner.subheadings[0] = 'Paziente Duplicato';
                } else {
                    this.bookingBanner.headerTitle = 'Nuova Prenotazione';
                    this.bookingBanner.subheadings = [];
                    this.bookingBanner.subheadings[0] = 'Nuovo Paziente';
                    return;
                }
            }
            this.bookingBanner.subheadings = [];
            if (item.startAddress.compoundAddress) {
                this.bookingBanner.subheadings[0] = '<p style="color: #b4bcc2;margin-bottom: 1px"><i class="fa fa-arrow-circle-right" aria-hidden="true"> </i>Partenza</p><p style="margin-bottom: 1px">' +
                    item.startAddress.compoundAddress + "</p>";
            }
            if (item.endAddress.compoundAddress) {
                this.bookingBanner.subheadings[1] = '<p style="color: #b4bcc2;margin-bottom: 1px"><i class="fa fa-map-marker" aria-hidden="true"> </i>Destinazione</p><p style="margin-bottom: 1px">' +
                    item.endAddress.compoundAddress + "</p>";
            }

            /*this.bookingBanner.subheadings[1] = 'Destinazione ' + item.startAddress.compactAddress;*/
            this.bookingBanner.optionItems = [];

            this.bookingBanner.optionItems.push({
                name: 'Trasporto',
                icon: 'fa fa-ambulance',
                values: [{
                    key: item.phase ? 'Fase' : '',
                    value: decodeCompact2Sd("PHASE", item.phase)
                },
                {
                    key: item.transportType ? 'Tipologia' : '',
                    value: item.transportType
                },
                {
                    key: item.assignedParkingCode ? 'Postazione' : '',
                    value: item.assignedParkingCode
                },
                {
                    key: item.priority ? 'Priorità' : '',
                    value: item.priority ? decodeCompact2Sd("PRIORITY", item.priority) : ''
                },
                {
                    "key": item.req.reference ? "Riferimento" : '',
                    "value": item.req.reference ? item.req.reference : ''
                },
                {
                    "key": item.req.convention ? "Convenzione" : '',
                    "value": item.req.convention ? item.req.convention : ''
                },
                {
                    key: item.equipmentList && item.equipmentList.length > 0 ? 'Attrezzatura' : '',
                    value: item.equipmentList && item.equipmentList.length > 0 ? this.formatEquipmentList(item.equipmentList) : ''
                }
                ]
            });

            this.bookingBanner.optionItems.push({
                name: 'Paziente',
                icon: 'fa-address-book',
                values: [{
                    key: calcolaTrasportato(item) ? 'Trasportato' : '',
                    value: calcolaTrasportato(item)
                },
                {
                    key: item.patientCompare ? 'Accompagnato' : '',
                    value: item.patientCompare
                },
                {
                    key: item.patientStatus ? 'Deambulazione' : '',
                    value: item.patientStatus
                }]
            });

            this.bookingBanner.optionItems.push({
                name: 'Informazioni prenotazione',
                icon: 'fa-info',
                values: [
                    {
                        key: item.transportDate ? 'Data trasporto' : '',
                        value: item.transportDate  ? decodePhaseForDate(item.phase,formatDate(item.transportDate, 'DD-MM-YYYY HH:mm')):""//formatDate(item.transportDate, 'DD-MM-YYYY HH:mm') : ''
                    },
                    {
                        key: this.descReturnDateField(item.returnDate),
                        value: this.formatReturnDateField(item.returnDate)
                    },
                    {
                        key: item.note ? 'Note' : '',
                        value: item.note ? item.note : ''
                    },
                    {
                        "key": item.indications ? "Indicazioni" : '',
                        "value": item.indications ? item.indications : ''
                    },
                    {
                        key: item.creationDate ? 'Creata il' : '',
                        value: item.creationDate ? formatDate(item.creationDate, 'DD-MM-YYYY HH:mm') : ''
                    }
                ]
            });

            this.bookingBanner.optionItems.push({
                name: 'Altre info',
                icon: 'fa-info',
                values: [
                    {
                        key: 'Archivio',
                        value: (item.source && item.source=='S') ? 'STORICO' : 'OnLine'
                    },
                    {
                        key: 'Prenotazione ciclica',
                        value: item.ciclicalId ? 'SI' : 'NO'
                    },
                    {
                        key: 'Prenotazione di Ritorno',
                        value: item.createdAsReturn ?  item.returnReady ? 'SI (non risvegliata)': 'SI' : 'NO'
                    },
                    {
                        key: item.modifyUsername ? 'Modificata da' : '',
                        value: item.modifyUsername ? item.modifyUsername : ''
                    },
                    {
                        key: item.deleteUsername ? 'Cancellata da' : '',
                        value: item.deleteUsername ? item.deleteUsername : ''
                    },
                    {
                        key: item.deletedate ? 'Cancellata il' : '',
                        value: item.deletedate ? formatDate(item.deletedate, 'DD-MM-YYYY HH:mm') : ''
                    },
                    {
                        key: item.deleteReason ? 'Motivo cancellazione' : '',
                        value: item.deleteReason ? item.deleteReason : ''
                    }
                ]
            });



            //                item.equipmentList.forEach( v => {
            //                    this.bookingBanner.optionItems.push( new BannerDataGroup() );
            //                } );
        } else {
            this.bookingBanner.headerTitle = 'Nuova Prenotazione';
            this.bookingBanner.subheading = 'Nuovo Paziente ';
        }
    }

    triggerSubmit() {

    }

    private descReturnDateField(returnDate): string {
        let dataRitornoFlag = this.sdSvc.getConfigValueByKey('BOOKING_DATA_RITORNO', '0');
        dataRitornoFlag = dataRitornoFlag == '1';

        if (!returnDate && dataRitornoFlag) {
            return '';
        }

        if (dataRitornoFlag) {
            return 'Data Ritorno';
        } else {
            return 'Prevede Ritorno';
        }
    }

    private formatReturnDateField(returnDate): string {
        let dataRitornoFlag = this.sdSvc.getConfigValueByKey('BOOKING_DATA_RITORNO', '0');
        dataRitornoFlag = dataRitornoFlag == '1';

        if (!returnDate && dataRitornoFlag) {
            return '';
        }

        if (!dataRitornoFlag) {
            if (returnDate) {
                return 'SI';
            }
            return 'NO';
        }

        return defIfEmpty(formatDate(returnDate, 'DD-MM-YYYY HH:mm'))
    }

    private formatEquipmentList(eqList: Array<Value>): string {
        let list: string = '';
        if (eqList && eqList.length > 0) {
            eqList.forEach(element => {
                list += element.name + " - ";
            });
            if (list != '') {
                list = list.substring(0, list.length - 3);
            }
        }
        return defIfEmpty(list);
    }


}
