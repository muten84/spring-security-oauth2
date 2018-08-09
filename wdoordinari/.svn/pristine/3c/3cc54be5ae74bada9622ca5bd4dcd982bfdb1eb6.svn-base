import { Routes, RouterModule } from '@angular/router';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MenuItem } from '../model/menu-item';
/*import * as _ from "lodash";*/
/*import isUndefined from 'lodash/isUndefined'*/
import * as _ from 'lodash'


@Component({
    selector: 'menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

    @Input() items: Array<MenuItem>;
    @Output() public collapse: EventEmitter<any> = new EventEmitter();


    public side: boolean = false;
    private layoutApp = <HTMLElement[]><any>document.getElementsByClassName('layout-app');


    public updateMenu() {
        if (this.side === true) {
            for (let layout of this.layoutApp) {
                layout.classList.add("collapsed-menu");
            }
        } else {
            for (let layout of this.layoutApp) {
                layout.classList.remove("collapsed-menu");
            }
        }
        this.collapse.emit(this.side);
    }

    ngOnInit(): void {
        //  this.getRegions();
        //this.showEntries();
    }

    //https://angular.io/guide/lifecycle-hooks
    ngAfterViewInit(): void {
        let menu = <HTMLElement><any>document.getElementById('menu');
        if (_.isUndefined(menu)) {
            return;
        }
        let childs = <HTMLElement[]><any>menu.getElementsByClassName("menu-child");
        for (let c of childs) {
            this.showEntries(c.id);
        }

    }

    public showEntries(id: string): void {
        let menuEntry = <HTMLElement><any>document.getElementById(id);
        if (menuEntry == null) { return }
        menuEntry.classList.add("show");


    }

    constructor() {

    }

}