import { OnInit, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject, TemplateRef } from '@angular/core';
import { LocalBusService, Logger, DeviceOrientationService, ApplicationStatusService } from './service/service.module';
import { Router } from '@angular/router';
import { GridDialogComponent, ConfirmDialogComponent, MessageDialogComponent, ListItemDialogComponent, InfoDialogComponent, ListItemDialogNoSearchComponent, CalendarDialogComponent, ConfirmGridDialogComponent } from './tablet-layout/components/components.module';
import { ComponentType } from '@angular/cdk/portal';
import { Subscription } from 'rxjs/Subscription';
import { environment } from '../environments/environment';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  subs: Array<Subscription> = [];

  constructor(public appStatusService: ApplicationStatusService, private orientationService: DeviceOrientationService, private logger: Logger, public router: Router, public dialog: MatDialog, public bus: LocalBusService) {

  }

  ngOnInit() {
    this.logger.info("ng on init of app.component");
    this.subs.push(this.bus.addObserver('dialogs').subscribe((event) => {
      this.logger.info('received event: ' + event);
      if (event.type === 'confirmDialog') {
        this.openDialog(event.payload, event.type, ConfirmDialogComponent);
      }
      else if (event.type === 'listItemDialog') {
        this.openDialog(event.payload, event.type, ListItemDialogComponent);
      }
      else if (event.type === 'infoDialog') {
        this.openDialog(event.payload, event.type, InfoDialogComponent);
      }
      else if (event.type === 'messageDialog') {
        this.openDialog(event.payload, event.type, MessageDialogComponent);
      }
      else if (event.type === 'listItemDialogNoSearch') {
        this.openDialog(event.payload, event.type, ListItemDialogNoSearchComponent);
      }
      else if (event.type === 'calendarDialog') {
        this.openDialog(event.payload, event.type, CalendarDialogComponent);
      }
      else if (event.type === 'gridDialog') {
        this.openDialog(event.payload, event.type, GridDialogComponent);
      }
      else if (event.type === 'confirmGridDialog') {
        this.openDialog(event.payload, event.type, ConfirmGridDialogComponent);
      }
    }));
    this.subs.push(this.bus.addObserver('newIntervention').subscribe((event) => {
      this.router.navigate(['intervention/alertMessage']);
    }));
    this.subs.push(this.bus.addObserver('reloadIntervention').subscribe((event) => {
      this.router.navigate(['intervention/newIntervention']);
    }));
    this.subs.push(this.bus.addObserver('updateNotifyIntervention').subscribe((event) => {

      this.router.navigate(['intervention/alertMessage']);

    }));

    this.subs.push(this.bus.addObserver('clearIntervention').subscribe((event) => {

      this.router.navigate(['intervention/alertMessage']);

    }));

    this.router.navigate(['/loader']);
  }

  private openDialog<T>(data: any, sendTo: string, componentOrTemplateRef: ComponentType<T> | TemplateRef<T>): void {
    const dialogRef = this.dialog.open(componentOrTemplateRef, {
      width: environment.configuration.global.confirmDialogWidth,
      data: data
    });
    dialogRef.afterClosed().subscribe(result => {
      this.logger.info('The dialog was closed');
      this.bus.notifyAll(sendTo, { type: sendTo + 'Response', payload: result });
    });
  }

  ngOnDestroy() {
    this.bus.removeAllObservers('dialogs', this.subs);
  }

}
