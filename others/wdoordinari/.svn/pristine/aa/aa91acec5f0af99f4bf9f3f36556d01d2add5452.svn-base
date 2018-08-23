import { Pipe, PipeTransform } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';


/*
 * Converte una data in input in una NgbDateStruct
 * 
 * */
@Pipe( { name: 'dateStruct' } )
export class DateStructPipe implements PipeTransform {

    transform( value: Date ): NgbDateStruct | undefined {
        if ( value ) {
            if ( !( value instanceof Date ) ) {
                let v = new Date();
                v.setTime( value );
                value = v;
            }
            return {
                year: value.getFullYear(),
                month: value.getMonth() + 1,
                day: value.getDay()
            };
        }
    }
}
