import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { Input, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Component({
    selector: 'app-list-item',
    templateUrl: 'list-item.component.html',
    styleUrls: ['./list-item.component.scss']

})
export class ListItemComponent {

    @Input()
    itemList: Array<any>;

    @Input()
    cols: Number;

    @Input()
    rowHeight: String;


    @Output()
    itemSelected: EventEmitter<any> = new EventEmitter();


    title: string;
    items = [];
    selected: any;

    constructor() {

    }

    selectItem(data: any) {
        this.selected = data;
        this.itemSelected.emit(this.selected);
    }


    public doReset(proceed: boolean) {
        if (proceed) {
            this.selected = undefined;
        }
    }



}