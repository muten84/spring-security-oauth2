import { BookingSearchItem } from '../../sinottico-prenotazioni/sinottico-prenotazioni-model';
import { ServiceSearchItem } from '../../ricerca-servizi/ricerca-servizi-model';

export const SERVICES: ServiceSearchItem[] = [
    {
        bookingCode: '12345678',
        convention: "AUSLBON",
        dataDaA: "21/06/2017 15:30:00 PA",
        deamb: "SEDIA",
        startAddress: "BUDRIO MEDICINA ALA A",
        endAddress: "BUDRIO CAL DIALISI",
        transported: "PIPPO PLUTO",
        doc: false,
        type: "CONS",
        /* dropd: "<div class=\"dropdown\">" +
         "  <button class=\"btn btn-secondary dropdown-toggle\" type=\"button\" id=\"dropdownMenuButton\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">" +
         "    Dropdown button" +
         "  </button>" +
         "  <div class=\"dropdown-menu\" aria-labelledby=\"dropdownMenuButton\">" +
         "    <a class=\"dropdown-item\" href=\"#\">Action</a>" +
         "    <a class=\"dropdown-item\" href=\"#\">Another action</a>" +
         "    <a class=\"dropdown-item\" href=\"#\">Something else here</a>" +
         "  </div>" +
         "</div>"*/
        /*dropd: "<button class=\"btn btn-outline-success\" (click)=\"$event.stopPropagation(); myDrop.open();\"><i class=\"fa fa-external-link\" aria-hidden=\"true\"></i></button>"*/
        dropd: ""
    },
]

export const BOOKINGS: BookingSearchItem[] = [
    {
        bookingCode: '12345678',
        convention: "AUSLBON",
        dataDaA: "21/06/2017 15:30:00 PA",
        deamb: "SEDIA",
        startAddress: "BUDRIO MEDICINA ALA A",
        endAddress: "BUDRIO CAL DIALISI",
        transported: "PIPPO PLUTO",
        doc: false,
        type: "CONS",
        dropd: "",
        optionItems: [
            {
                name: "Modifica prenotazione",
                eventSource: "modifybooking",
                icon: "fa-pencil"
            },
            {
                name: "Crea servizio",
                eventSource: "modifybooking",
                icon: "fa-plus-circle"
            },
            {
                name: "Associa al servizio",
                eventSource: "modifybooking",
                icon: "fa-check-square"
            }],
        detail: {
            groups: [
                {
                    "name": "Prenotazione",
                    "icon": "fa-book",
                    values: [{
                        "key": "Codice",
                        "value": "12345678"
                    }
                    ]
                },
                {
                    "name": "Paziente",
                    "icon": "fa-user-circle",
                    values: [{
                        "key": "Codice",
                        "value": "12345678"
                    }
                    ]
                },
                {
                    "name": "Destinazione",
                    "icon": "fa-hospital-o",
                    values: [{
                        "key": "Codice",
                        "value": "12345678"
                    }
                    ]
                }
            ],


        }
    },

    {
        bookingCode: '12345679',
        convention: "AUSLBON",
        dataDaA: "21/06/2017 15:30:00 PA",
        deamb: "SEDIA",
        startAddress: "BUDRIO MEDICINA ALA A",
        endAddress: "BUDRIO CAL DIALISI",
        transported: "PIPPO PAPERINO",
        doc: false,
        type: "CONS",
        dropd: "",
        optionItems: [
            {
                name: "Modifica prenotazione",
                eventSource: "modifybooking",
                icon: "fa-pencil"
            },
            {
                name: "Crea servizio",
                eventSource: "modifybooking",
                icon: "fa-plus-circle"
            },
            {
                name: "Associa al servizio",
                eventSource: "modifybooking",
                icon: "fa-check-square"
            }],
        detail: {
            groups: [
                {
                    "name": "Prenotazione",
                    "icon": "fa-book",
                    values: [{
                        "key": "Codice",
                        "value": "12345679"
                    }
                    ]
                },
                {
                    "name": "Paziente",
                    "icon": "fa-user-circle",
                    values: [{
                        "key": "Codice",
                        "value": "12345679"
                    }
                    ]
                },
                {
                    "name": "Destinazione",
                    "icon": "fa-hospital-o",
                    values: [{
                        "key": "Codice",
                        "value": "12345679"
                    }
                    ]
                }
            ],


        }
    }


];