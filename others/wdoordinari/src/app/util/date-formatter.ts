import {NgbDate} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date';
import {NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {padNumber, toInteger, isNumber} from '@ng-bootstrap/ng-bootstrap/util/util';

export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  
  parse(value: string): NgbDateStruct {

   
    if (value) {
      const dateParts = value.trim().split('/');
      /*
     Put your logic for parsing
      */ 
      if (dateParts.length === 1 && isNumber(dateParts[0])) {
        return new NgbDate(null, null, toInteger(dateParts[0]));
      } else if (dateParts.length === 2 && isNumber(dateParts[0]) && isNumber(dateParts[1])) {
        return new NgbDate(null , toInteger(dateParts[1]),  toInteger(dateParts[0]) );
      } else if (dateParts.length === 3 && isNumber(dateParts[2]) && isNumber(dateParts[1]) && isNumber(dateParts[0])) {
        return new NgbDate(toInteger(dateParts[2]), toInteger(dateParts[1]) , toInteger(dateParts[0]));
      }
    }
    return null;
  }

  format(date: NgbDateStruct): string {
    return date ?
      `${isNumber(date.day) ? padNumber(date.day) : ''}/${isNumber(date.month) ? padNumber(date.month) : ''}/${date.year}` :
        '';
      
  }

}
