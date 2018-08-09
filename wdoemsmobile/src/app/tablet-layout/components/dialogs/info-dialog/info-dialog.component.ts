import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-info-dialog',
    templateUrl: 'info-dialog.component.html',
    styleUrls: ['./info-dialog.component.scss']

})
export class InfoDialogComponent {
    title: string;
    content: string;
    justify: boolean;

    constructor(
        public dialogRef: MatDialogRef<InfoDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.content = data.content;
        this.justify = data.justifyContent;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

}