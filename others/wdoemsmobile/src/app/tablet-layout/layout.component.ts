import { OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject, TemplateRef } from '@angular/core';
import { LocalBusService } from '../service/service.module';
import { Router } from '@angular/router';
import { ConfirmDialogComponent, ListItemDialogComponent, ListItemDialogNoSearchComponent  } from './components/components.module';
import { ComponentType } from '@angular/cdk/portal';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutTabletComponent implements OnInit, OnDestroy {

  @BlockUI('main-section') blockUI: NgBlockUI;

  maskSubscription: Subscription;

  constructor(public router: Router, public dialog: MatDialog, public bus: LocalBusService) {

  }

  private mask() {
    this.blockUI.start();
  }

  private unmask() {
    this.blockUI.stop();
  }

  ngOnInit() {

    this.maskSubscription = this.bus.addObserver("masks").subscribe((event) => {

      if (event.type === 'mask') {
        this.mask();
      }
      if (event.type === 'unmask') {
        this.unmask();
      }
    }
    );
  }



  ngOnDestroy() {
    this.bus.removeAllObservers('masks', [this.maskSubscription]);
  }





}
