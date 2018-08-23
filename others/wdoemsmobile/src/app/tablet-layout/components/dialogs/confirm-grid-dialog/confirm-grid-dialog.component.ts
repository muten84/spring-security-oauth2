import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Sort, MatTableDataSource, MatSort } from '@angular/material';

export interface Interview2Element {
    name: string;
    subgroup: string;
    group: string;
}

@Component({
    selector: 'app-confirm-grid-dialog',
    templateUrl: 'confirm-grid-dialog.component.html',
    styleUrls: ['./confirm-grid-dialog.component.scss']

})
export class ConfirmGridDialogComponent {
    title: string;
    content: string;
    justify: boolean;

    columnsToDisplay = ['subgroup', 'name'];

    items: Array<Interview2Element> = [];
    sortedData = [];
    dataSource: MatTableDataSource<Interview2Element>;
    groupedItems = [];
    currIndex = 0;
    nextGroup = "";

    @ViewChild(MatSort) sort: MatSort;

    constructor(
        public dialogRef: MatDialogRef<ConfirmGridDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        dialogRef.disableClose = true;
        this.title = data.title;
        this.content = data.content;
        this.justify = data.justifyContent;
        this.items = data.items;
        if (data.items) {
            // this.sortedData = data.items.slice();
            this.dataSource = new MatTableDataSource(data.items);
            this.dataSource.sort = this.sort;
            let arr: Array<any> = data.items;
            let map: Map<string, any> = new Map<string, any>();
            for(var y=0; y<arr.length; y++){
                let currGroup  = arr[y].group;
               
                if (!map.get(currGroup)) {
                    map.set(currGroup, []);
                }else{
                    arr[y].subgroup = "";
                }

                map.get(currGroup).push(arr[y]);
            }
          /*  this.groupedItems = arr.reduce((prev, curr, currI, result) => {

                if (!map.get(curr.group)) {
                    map.set(curr.group, []);
                }else{
                    curr.subgroup = "";
                }
                map.get(curr.group).push(curr);

                return map;
            });*/
           /* let cnt = 1;
            let keys = Array.from(map.keys());
            this.groupedItems = [];
            keys.forEach((k) => {
                this.groupedItems[cnt] = map.get(k);
                cnt++;
            });*/
            //console.log("items", this.groupedItems);
        }

    }



    onNoClick(): void {
        this.dialogRef.close();
    }

    nextStep() {
        ////console.log("next is: " + subgroup);
        this.currIndex = (this.currIndex + 1) % this.groupedItems.length;
        this.nextGroup = this.groupedItems[this.currIndex][0].group;
    }

    setCurrent(sub) {
        this.currIndex = this.groupedItems.findIndex((val, i, arr) => {
            // return arr[i].subgroup === sub;
            return val[0].group === sub
        })
        //console.log("setting curr index to: " + this.currIndex);
        //this.nextGroup = this.groupedItems[this.currIndex][0].subgroup;

    }

}

function compare(a, b, isAsc) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}