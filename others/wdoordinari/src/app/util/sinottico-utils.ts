import { MenuItemValue, Value } from "services/services.module";
import { BookingDTO, PatientBookingDTO } from "services/services.module";
import { valueToSelect } from 'app/util/convert';
import { StaticDataService } from "app/static-data/cached-static-data";
import * as _ from 'lodash';

export function mapIdWithIcons(iconId) {
    //console.log('mapIdWithIcons');
    ////console.log("icon id is: " + iconId);
    switch (iconId) {
        case "modifybooking":
            return "fa-pencil";
        case "viewbooking":
            return "fa-info-circle";
        case "ADDRESS_DETAIL":
            return "fa-address-book-o";
        case "CREA_SERVIZIO":
            return "fa-plus-circle";
        case "AGGIUNGI_PRENOTAZIONE":
            return "fa-calendar-check-o";
        case "AGGIUNGI_AI_MEZZI":
            return "fa-ambulance";
        case "POPUP_ELIMINA_PRENOTAZIONE":
            return "fa-trash";
        case "POPUP_RENDI_CICLICA":
            return "fa-gg";
        case "MOVE_TO_RETURNS":
            return "fa-arrow-circle-o-left";
        case "MOVE_TO_BOOKINGS":
            return "fa-arrow-circle-o-right";
        case "ITEM_MANUTENCOOP_BOOKING_STATUS":
            return "";
        case "SHOW_HISTORY":
            return "fa-list-alt";
        case "SEND_BOOKING_TIU":
            return "fa-paper-plane-o";
        case "MULTIPLE_DUPLICATE":
            return "fa-plus-circle";
        case "assignVehicle":
            return "fa-ambulance";
        case "archiveService":
            return "fa-archive";
        case "ARCHIVE_BOOKING":
            return "fa-archive";
        case "ADD_BOOKING_TO_SERVICE":
            return "fa-archive";
        case "synoptic":
            return "fa-th";
        case "serviceDetail":
            return "fa-info-circle";
        case "viewbookingRef":
            return "fa-sign-in"
    }
    return "";
}



export function convertMenuItem(mi: MenuItemValue): any {
    //console.log('convertMenuItem');
    let ret = {
        name: mi.name,
        eventSource: mi.id,
        icon: mapIdWithIcons(mi.id),
        position: mi.position,
        enabled: mi.enable
    }
    return ret;
}

export function calcolaTrasportato(booking: BookingDTO): string {
    let pb: PatientBookingDTO = booking.patientBooking;
    let trasportatoPaziente = pb.name ? pb.name + ' ' + pb.surname : '';
    let trasportatoSangue = booking.blood ? 'SANGUE, ' : '';
    let trasportatoOrgani = (booking.organsFlag || booking.variousDetail) ? (booking.variousDetail ? booking.variousDetail + ', ' : '') : '';
    let trasportato = trasportatoSangue + trasportatoOrgani + trasportatoPaziente;
    if (trasportato.endsWith(", ")) {
        trasportato = trasportato.substring(0, trasportato.length - 2);
    }
    return trasportato;
}

export function  decodeCompact2Sd(type: string, compact: string): string {
    //console.log('decodeCompact2Sd');
    if (!localStorage.getItem("staticDataMap")) {
        return;
    }
    let values: Value[] = JSON.parse(localStorage.getItem("staticDataMap"))[type];
    ////console.log("values: " + values.length);
    let o = _.find(values, (o: Value) => {
        // //console.log("Compact is: " + o.compactDescription + " - " + compact);
        return o.compactDescription === compact;
    })
    if (!o) {
        return "";
    }
    return o.description;
}

export function decodePhaseForDate(phase: string, transportDate: string): string {
    //console.log('decodePhaseForDate');
    switch (phase) {
        case "0":
            return transportDate + " PA";
        case "1":
            return transportDate + " DE";
        case "2":
            return transportDate.split(" ")[0] + " ND"

    }
    return "";
}


export function getBookingImage(pattern: string, ciclicalId: string, source:string): string {
    let isReturn = contains(pattern, "ritorno");
    let isAndata = contains(pattern, "andata");
    let isLate = contains(pattern, "ritardo");
    let isAlarm = contains(pattern, "allarme");
    let isWarning = contains(pattern, "avviso");
    let isNoRevival = contains(pattern, "risveglio");
    let isHistoryArchive = source && source=='S';
    
    let retIcons = '';
    //let titleAltText= '';
    let color = ' color: #b4bcc2; ';
    let styleBase=' border-radius: 27px; ';//27px
    let background =' background-color: #041c28; ';
    
    //Default value
    let padding= ' padding: 1px 3px 1px 3px !important; '; //non default
    let fontSize=' font-size: 1.3rem; '; //default fontSize
    
    let classIcon = 'class="'; //init class icon name

    if (isWarning) {
        background =' background-color: #ffb14c; ';
        color= ' color: #7c8b8c;';
    } 
    

    if (isAndata && !isReturn) {
        classIcon +='fa fa-arrow-circle-right" '
    }
    else {
        if (ciclicalId) {
            classIcon +='fa fa-refresh" ';
            padding= ' padding: 3px 3px 2px 3px !important; '; 
            fontSize=' font-size: 1.1rem; ';
        }else if(isNoRevival){
            classIcon +='fa fa-mail-reply" ';
            padding= ' padding: 3px 3px 3px 3px !important; '; 
            fontSize=' font-size: 1rem; ';
        }else{
            classIcon +='fa fa-arrow-circle-left" ';
        }
    }

    retIcons = '<i style="'+ styleBase + padding+ fontSize + background + color+'" '+ classIcon + ' aria-hidden="true"></i>';

    if (isAlarm) {
        //ret += '<i style="padding: 0px; color: #072c3f; font-size: 0.9rem; border-radius: 27px" class="fa fa-clock-o" alt="Allarmato" title="Allarmato"></i>';
        retIcons += '<i style="padding: 0px; color: #072c3f; font-size: 0.9rem; border-radius: 27px" class="fa fa-clock-o"></i>';
    }
    if (isLate) {
        //ret += '<i style="padding: 0px; color: #eb1b4c; font-size: 0.9rem; border-radius: 27px" class="fa fa-exclamation" alt="In ritardo" title="In ritardo"></i>';
        retIcons += '<i style="padding: 0px; color: #eb1b4c; font-size: 0.9rem; border-radius: 27px" class="fa fa-exclamation"></i>';
    }
    if(isHistoryArchive){
        //ret += '<i style="padding: 0px; color: #eb1b4c; font-size: 0.9rem; border-radius: 27px" class="fa fa-exclamation" alt="In ritardo" title="In ritardo"></i>';
        retIcons += '<i style="padding: 0px; color: #eb1b4c; font-size: 0.8rem;"> S</i>';
    }
    return retIcons;//ret;
}

export function  getBookingTooltipImage(pattern: string, ciclicalId: string, source:string): string{
    let isReturn = contains(pattern, "ritorno");
    let isAndata = contains(pattern, "andata");
    let isLate = contains(pattern, "ritardo");
    let isAlarm = contains(pattern, "allarme");
    let isWarning = contains(pattern, "avviso");
    let isNoRevival = contains(pattern, "risveglio");
    let isHistoryArchive = source && source=='S';

    let titleAltText= '';

        if (isAlarm) {
            titleAltText= ' in allarme';           
        } else if (isLate) {
            titleAltText= ' in ritardo';
        }

        if (isWarning) {
            titleAltText= titleAltText + ' con storico modifiche';           
        }

        if (isAndata && !isReturn) {
            titleAltText = 'Andata' + titleAltText;           
        }
        else {
            if (ciclicalId) {
                titleAltText="Ciclica"+ titleAltText;
               
            }else if(isNoRevival){
                titleAltText = 'Ritorno non risvegliato' + titleAltText;
               
            }else{
                titleAltText = 'Ritorno' + titleAltText;
                
            }
        }
        if(isHistoryArchive){
            titleAltText = "Archivio Storico - " + titleAltText;
        }

    return titleAltText;
}

function contains(pattern, val): boolean {
    let matchResult = pattern.toLowerCase().match(val);
    if (matchResult) {
        return matchResult.length > 0
    }
    return false;
}


