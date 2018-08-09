import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-message-dialog',
    templateUrl: 'message-dialog.component.html',
    styleUrls: ['./message-dialog.component.scss']

})
export class MessageDialogComponent {
    title: string;
    content: string;

    constructor(
        public dialogRef: MatDialogRef<MessageDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.content = data.content;
    }

    confirm() {
        this.dialogRef.close();
    }


}