import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { LocalBusService, ResourceListService} from '../../../../service/service.module';
import { Router } from '@angular/router';


export interface Event {
    type?: string;
    payload?: any;
}

@Component({
    selector: 'app-workflow-status',
    templateUrl: './workflow-status.component.html',
    styleUrls: ['./workflow-status.component.scss']
})



export class WorkflowStatusComponent implements OnInit, OnDestroy {
    isActive: boolean = false;
    showMenu: string = '';

    @Input('items')
    items;

    @Input()
    sdisabled;

    @Input()
    sreadonly;

    @Input()
    sactive;

    @Output()
    onNewStatus = new EventEmitter();

    constructor(public bus: LocalBusService, private resourceService: ResourceListService, public router: Router) {

    }

    ngOnInit() {

    }

    ngOnDestroy() {

    }

    statusClicked(status) {
        this.onNewStatus.emit(status);
    }


}
