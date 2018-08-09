export class RicercaBookingFilter {
    code: string;
    name: string;
    lastname: string;
    others: string;
    fromDate: Date;
    toDate: Date;

    constructor() { }

    public toString() {

        return 'RicercaBookingFilter (name: ${this.name}, lastname: $(this.lastname) ) ';
    }
}

export class BookingSearchItem {
    bookingCode: string;
    dataDaA: string;
    type: string;
    deamb: string;
    convention: string;
    startAddress: string;
    endAddress: string;
    transported: string;
    doc: boolean;
    dropd: string;
    detail: any;
    optionItems: any;

    constructor() { }
}

export class Item {
  constructor(public name: string, public value: string) { 
  }
} 
