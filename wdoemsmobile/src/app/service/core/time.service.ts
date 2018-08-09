import { Inject, Injectable, Optional } from '@angular/core';
import { Http, Headers, URLSearchParams } from '@angular/http';

@Injectable()
export class TimeService {



    getCurrentTime(): Promise<Date> {
        /*return this.http.get(this.currentPriceUrl).toPromise()
          .then(response => response.json().bpi[currency].rate);*/
        const p: Promise<Date> = new Promise<Date>((resolve, reject) => {
            resolve(new Date());
        });
        return p;
    }

    async getAsyncCurrentTime(): Promise<Date> {
        const response: Date = await this.getCurrentTime();
        return response;
    }

}
