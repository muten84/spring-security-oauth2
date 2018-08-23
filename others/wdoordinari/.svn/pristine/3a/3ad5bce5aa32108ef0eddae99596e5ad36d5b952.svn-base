
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Value } from 'services/services.module';
import * as moment from 'moment';

export function valueToSelect(list: Array<Value>): Array<any> {
    return list.map(valueToSelectMap);
}

export function valueToSelectMap(v: Value): any {
    (<any>v).text = v.description
    return v;
}

export function valueNameToSelect(list: Array<Value>): Array<any> {
    return list.map(v => {
        (<any>v).text = v.name
        return v;
    });
}


export function valueToString(list: Array<Value>): Array<string> {

    return list.map(v => {
        return v.compactDescription || v.description;
    });

}

export function convertToDate(event: NgbDateStruct, time: string): Date | undefined {
    if (event && event.year !== NaN && event.month !== NaN && event.day !== NaN) {
        let d = new Date();
        d.setFullYear(event.year, event.month - 1, event.day);

        if (time) {
            let hhmm = time.split(':');
            if (hhmm.length > 0) {
                d.setHours(Number(hhmm[0]), Number(hhmm[1]), 0, 0);
            }
        }

        return d;
    }
}

export function convertToString(value: Date): string | undefined {
    if (value) {
        let m = moment(value);
        return m.format('HH:mm');
    }
}


export function convertToStruct(value: Date): NgbDateStruct | undefined {
    if (value) {
        if (!(value instanceof Date)) {
            let v = new Date();
            v.setTime(value);
            value = v;
        }
        return {
            year: value.getFullYear(),
            month: value.getMonth() + 1,
            day: value.getDay()
        };
    }
}

export function convertMomentDateToStruct(m): NgbDateStruct | undefined {

    let ret: NgbDateStruct = {
        day: m.date(), month: (m.month() + 1), year: m.year()
    };
    return ret;
}


export function formatDate(d, format?): string {
    if (!d) {
        return "";
    }
    let m = moment(d);
    let formatDate = "";
    if (!format) {
        formatDate = m.format('DD-MM-YYYY')
    }
    else {
        formatDate = m.format(format);
    }
    return formatDate;
}


export function defIfEmpty(val): string {
    if (val) {
        return val;
    }
    return "n.d.";
}

export function sameDay(d1: Date, d2: Date): boolean {
    return d1.getFullYear() == d2.getFullYear()
        && d1.getMonth() == d2.getMonth()
        && d1.getDate() == d2.getDate();
}


export function dateToNgbStruct(date: moment.Moment): NgbDateStruct {
    if (!date) {
      return null;
    }
    return {  day: date.day(), month: date.month()-1, year: date.year(), };
  }

export function ngbStructToDate(date: NgbDateStruct, pattern: string): moment.Moment {
    if (!date) {
      return null;
    }
    return moment(date.day + '/' + date.month + '/' + date.year, pattern);
  }




