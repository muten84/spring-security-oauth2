import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-list-item-dialog-no-search',
    templateUrl: 'list-item-dialog-no-search.component.html',
    styleUrls: ['./list-item-dialog-no-search.component.scss']

})
export class ListItemDialogNoSearchComponent {

    title: string;
    items = [];
    nCols: number;
    selected: any;

    constructor(
        public dialogRef: MatDialogRef<ListItemDialogNoSearchComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.items = data.items;
        this.nCols = data.nCols;
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