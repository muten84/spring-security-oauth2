import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-calendar-dialog',
    templateUrl: 'calendar-dialog.component.html',
    styleUrls: ['./calendar-dialog.component.scss']

})
export class CalendarDialogComponent {
    title: string;
    content: string;

    constructor(
        public dialogRef: MatDialogRef<CalendarDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.content = data.content;
    }

    onNoClick(): void {
        this.dialogRef.close();
    } 
 
    confirm() {
        this.dialogRef.close(this.content);
    }

}