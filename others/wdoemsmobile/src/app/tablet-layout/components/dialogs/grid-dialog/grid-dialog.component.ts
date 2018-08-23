import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Component, Inject } from '@angular/core';
import { OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Sort, MatTableDataSource, MatSort } from '@angular/material';

export interface InterviewElement {
    name: string;
    group: string;
    subgroup: string;
}

@Component({
    selector: 'app-grid-dialog',
    templateUrl: 'grid-dialog.component.html',
    styleUrls: ['./grid-dialog.component.scss']

})
export class GridDialogComponent {
    title: string;
    content: string;
    justify: boolean;

    columnsToDisplay = ['group', 'subgroup', 'name'];

    items: Array<InterviewElement> = [];
    sortedData = [];
    dataSource: MatTableDataSource<InterviewElement>;
    groupedItems = [];
    currIndex = 0;
    nextGroup = "";

    @ViewChild(MatSort) sort: MatSort;

    constructor(
        public dialogRef: MatDialogRef<GridDialogComponent>,
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
            if(arr && arr.length >0){
                this.groupedItems = arr.reduce((prev, curr, currI, result) => {

                    if (!map.get(curr.subgroup)) {
                        map.set(curr.subgroup, []);
                    }
                    map.get(curr.subgroup).push(curr);

                    return map;
                });
                let cnt = 0;
                let keys = Array.from(map.keys());
                this.groupedItems = [];
                keys.forEach((k) => {
                    this.groupedItems[cnt] = map.get(k);
                    cnt++;
                });
            }
            //console.log("items", this.groupedItems);
        }

    }



    /*  sortData(sort: Sort) {
         const data = this.items.slice();
         if (!sort.active || sort.direction == '') {
             this.sortedData = data;
             return;
         }
 
         this.sortedData = data.sort((a, b) => {
             let isAsc = sort.direction == 'asc';
             switch (sort.active) {
                 case 'sintomo': return compare(a.name, b.name, isAsc);
                 case 'gruppo': return compare(+a.calories, +b.calories, isAsc);
                 default: return 0;
             }
         });
     } */

    onNoClick(): void {
        this.dialogRef.close();
    }

    nextStep() {
        ////console.log("next is: " + subgroup);
        this.currIndex = (this.currIndex + 1) % this.groupedItems.length;
        this.nextGroup = this.groupedItems[this.currIndex][0].subgroup;
    }

    setCurrent(sub) {
        this.currIndex = this.groupedItems.findIndex((val, i, arr) => {
            // return arr[i].subgroup === sub;
            return val[0].subgroup === sub
        })
        //console.log("setting curr index to: " + this.currIndex);
        //this.nextGroup = this.groupedItems[this.currIndex][0].subgroup;

    }

}

function compare(a, b, isAsc) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}