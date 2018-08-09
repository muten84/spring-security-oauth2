import { Router, NavigationEnd } from "@angular/router";
import { Subscription } from "rxjs";
import { OnInit, OnDestroy } from "@angular/core";



// classe astratta usata per gestire il comportamento del router di angular

export abstract class RouterComponent implements OnInit, OnDestroy {

    protected navigationSubscription: Subscription

    protected firstLoad = true;

    constructor(
        protected router: Router,
    ) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            if (e instanceof NavigationEnd) {
                if (!this.firstLoad) {
                    this.loadData();
                }
            }
        });
    }

    abstract loadData();

    ngOnInit() {
        this.loadData();
        this.firstLoad = false;
    }

    ngOnDestroy() {
        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
    }
}