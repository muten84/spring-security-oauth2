import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-confirm-dialog',
    templateUrl: 'confirm-dialog.component.html',
    styleUrls: ['./confirm-dialog.component.scss']

})
export class ConfirmDialogComponent {
    title: string;
    content: string;

    constructor(
        public dialogRef: MatDialogRef<ConfirmDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.content = data.content;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

}