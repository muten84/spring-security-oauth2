import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ComponentHolderService } from "app/service/shared-service";
import { BookingModuleServiceService, BookingDTO } from "services/services.module";
import { CalendarEvent } from "calendar-utils";
import { CalendarEventTimesChangedEvent, CalendarMonthViewDay, DAYS_OF_WEEK } from "angular-calendar";
import { Subject } from "rxjs/Subject";
import { convertToDate, sameDay } from "app/util/convert";
import { NgbDateStruct } from "@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct";
import { Router } from "@angular/router";
import * as moment from 'moment';

// è stato utilizzato il calendario:
// https://mattlewis92.github.io/angular-calendar/
@Component({
    selector: 'duplica-multipla-prenotazioni-component',
    templateUrl: './duplica-multipla-prenotazioni-component.html'
})
export class DuplicaMultiplaPrenotazioneComponent implements OnInit {

    currentId: string;
    //Configurazioni per il calendario
    viewDate: Date = new Date();
    events: CalendarEvent[] = [];
    refresh: Subject<any> = new Subject();
    activeDayIsOpen: boolean = false;
    days: Array<Date> = [];
    view = 'month';
    locale = 'it';
    weekStartsOn: number = DAYS_OF_WEEK.MONDAY;
    weekendDays: number[] = [DAYS_OF_WEEK.SUNDAY, DAYS_OF_WEEK.SATURDAY];
    //
    intervalFrom: NgbDateStruct;
    intervalTo: NgbDateStruct;

    selectedDays: boolean[] = [];
    //
    daysOfWeek = DAYS_OF_WEEK;
    allDay: boolean;
    // 
    model: BookingDTO;

    constructor(
        private route: ActivatedRoute,
        private componentService: ComponentHolderService,
        private bookingService: BookingModuleServiceService,
        private router: Router,
    ) {
    }

    ngOnInit(): void {
        this.currentId = this.route.snapshot.params['id'];

        if (this.currentId) {
            this.componentService.getRootComponent().mask();
            this.bookingService.getBookingById(this.currentId).subscribe(val => {
                this.model = val.result;
                if (this.componentService.getHeaderComponent('headerComponent')) {
                    (<any>this.componentService.getHeaderComponent('headerComponent')).fromItemToBannerModel(val.result);
                }
                this.componentService.getRootComponent().unmask();
            });
        }
    }

    duplicate() {
        if (this.days && this.days.length > 0) {

            this.componentService.getRootComponent().mask();
            var transportDate = moment(this.model.transportDate);

            this.days.forEach(e => {
                e.setHours(transportDate.hours());
                e.setMinutes(transportDate.minutes());
                e.setSeconds(transportDate.seconds());
            });

            //setto che posso duplicare se trovo già altre prenotazioni duplicate
            this.model.duplicable = true;
            var toSave = {
                booking: this.model,
                dates: this.days
            };
            this.bookingService.duplicateBooking(toSave).catch((o, t) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(res => {
                this.componentService.getRootComponent().unmask();
                this.router.navigate(['/sinottico-prenotazioni']);
            });
        } else {
            var message = 'Selezionare almeno un giorno dal calendario';
            this.componentService.getRootComponent().showToastMessage('error', message);
        }
    }

    clean() {
        this.selectedDays = [];
        this.intervalFrom = null;
        this.intervalTo = null;
        this.allDay = false;
        this.days = [];
        //effettuo il refresh del calendario
        this.refresh.next();
    }


    setDate(day: Date) {
        this.viewDate = day
    }

    checkAll(event) {
        this.allDay = true;
        for (let d in DAYS_OF_WEEK) {
            this.selectedDays[d] = event;
        }
        this.updateInterval();
    }

    checkDay(event, daysOfWeek: DAYS_OF_WEEK) {
        this.allDay = false;
        this.selectedDays[daysOfWeek] = event;
        this.updateInterval();
    }

    updateInterval() {
        //resetto la lista delle date
        this.days = [];

        if (this.intervalFrom && this.intervalTo) {
            var from = convertToDate(this.intervalFrom, "");
            var to = convertToDate(this.intervalTo, "");
            //ciclo su tutti i giorni nell'intervallo
            for (var d = from; d <= to; d.setDate(d.getDate() + 1)) {
                //se il giorno è inclusi tra tutti
                if (this.selectedDays[d.getDay()]) {
                    this.addDay(new Date(d.getTime()));
                }
            }
        }
        //effettuo il refresh del calendario
        this.refresh.next();
    }

    //metodo per il rendering delle celle
    beforeMonthViewRender({ body }: { body: CalendarMonthViewDay[] }): void {
        body.forEach(day => {
            if (this.isPresent(day.date)) {
                day.cssClass = 'cal-day-selected';
            }
        });
    }

    isPresent(date: Date): boolean {
        var isPresent = false;
        this.days.forEach(d => {
            if (sameDay(d, date)) {
                isPresent = true;
            }
        });
        return isPresent
    }

    //Aggiunge un giorno alla lista se non è già presente
    addDay(day: Date) {
        if (!this.isPresent(day)) {
            this.days.push(day);
        }
    }

    //aggiunge un giorno se non è presente, se è presente lo toglie
    addRemoveDay(day: Date) {
        if (!this.isPresent(day)) {
            this.days.push(day);
        } else {
            this.days = this.days.filter(d => !sameDay(d, day));
        }
    }

    // ------------ Eventi 
    dayClicked(day: CalendarMonthViewDay): void {
        console.log("dayClicked  " + day.date);
        if (day.cssClass == 'cal-day-selected') {
            day.cssClass = null;
        } else {
            day.cssClass = 'cal-day-selected';
        }
        this.allDay = false;
        for (let d in DAYS_OF_WEEK) {
            this.selectedDays[d] = false;
        }
        this.addRemoveDay(day.date);
    }

    handleEvent(action: string, event: CalendarEvent): void {
        console.log("handleEvent  " + action + " " + event);
    }

    eventTimesChanged({
        event,
        newStart,
        newEnd
      }: CalendarEventTimesChangedEvent): void {
        console.log("eventTimesChanged  " + event + " " + newStart + " " + newEnd);
    }
}