import { Injectable, EventEmitter } from '@angular/core';
/*import {Observable,Observer} from 'rxjs/Observable';*/
import { Observable, Observer, BehaviorSubject, Subject } from 'rxjs/Rx';
import 'rxjs/add/operator/share';
import { BookingDTO, OverviewBookingDTO, OverviewBookingFilterDTO } from '../../services/services.module';

import { ServiceSearchItem } from '../ricerca-servizi/ricerca-servizi-model';

import { RicercaServiziFilter } from '../ricerca-servizi/ricerca-servizi-model'
/*import { SinotticoPrenotazioniTableComponent } from '../sinottico-prenotazioni/sinottico-prenotazioni-table.component'*/
/*import * as _ from "lodash";*/
/*import {find} from "@types/lodash";*/
import * as _ from 'lodash'
import { Http, Response, RequestOptions, Headers } from '@angular/http';
/* import { ExtendedHttpService } from './extended-http.service';*/
import { BackendConfigService } from '../service/config.service';

@Injectable()
export class BookingService {

    _searchResult: EventEmitter<string> = new EventEmitter();

    /*static _tableComponent: RicercaPrenotazioniTableComponent;*/

    // TODO: usare questo !!!! http://blog.angular-university.io/how-to-build-angular2-apps-using-rxjs-observable-data-services-pitfalls-to-avoid/
    /*this.resultChange$ = new Observable(observer =>
        this._observer = observer).share();*/

    constructor(private http: Http, private config: BackendConfigService) {

    }

    searchServices(filter: RicercaServiziFilter): Observable<ServiceSearchItem[]> {
        // console.log("call searhcServices: " + filter);
        // to do
        return Observable.never();
    }

    searhcBookings(filter: OverviewBookingFilterDTO): Observable<OverviewBookingDTO[]> {
        let headers = new Headers({ 'Content-Type': 'application/x-www-form-urlencoded' }); // ... Set content type to JSON
        let options = new RequestOptions({ headers: headers }); // Create a request option

        let url = this.config.getUrlOperation("booking/searchBooking");

        return this.http.post(url, filter, options)
            // ...and calling .json() on the response to return data
            .map((response: Response) => response.json());
    }


    getBookingByCode(code: string): Observable<BookingDTO> {
        //to do
        return Observable.never();
    }

    setSearchResult(data) {

        this._searchResult.emit("size is" + data.length);
    }


}