import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-list-item-dialog',
    templateUrl: 'list-item-dialog.component.html',
    styleUrls: ['./list-item-dialog.component.scss']

})
export class ListItemDialogComponent {

    title: string;
    items = [];
    selected: any;

    constructor(
        public dialogRef: MatDialogRef<ListItemDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.items = data.items;
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    itemSelected(data: any) {

        this.selected = data;
    }

    confirm() {
        this.dialogRef.close(this.selected);
    }

}