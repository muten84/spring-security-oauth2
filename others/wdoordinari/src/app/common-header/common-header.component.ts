import { Component, EventEmitter, HostListener, Inject, InjectionToken, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, Routes } from '@angular/router';
import { ComponentHolderService, EventService } from '../service/shared-service';

export const ROUTES = new InjectionToken<Routes>('routes');

@Component({
    selector: 'commonheader',
    templateUrl: './common-header.component.html',
    styleUrls: ['./common-header.component.css'],
})
export class CommonHeaderComponent implements OnInit {

    @Input() rootComponent;

    contentForm = null;
    contentAll = null;
    headerTitle: string;
    headerIcon: string;
    showHeaderHook: boolean;
    showInnerComponentOnly: boolean;
    isForSearch: boolean;

    @Output() onSearchResult = new EventEmitter();

    outputs = {
        searchResult: (e) => {
            //console.log('Event received in common-header searchResult: ' + e)
            // this.onSearchResult.emit(e);
            this.componentService.getMiddleComponent('currentSearchTable').triggerSubmit([]);
        },
        /*currentBookingCode: (e) => {
            //console.log('Event received in common-search currentBookingCode: ' + e)
        }*/

        // this.searchResult.emit(e)
        // //console.log('Event received')
    }

    inputs = {
        currentBookingId: ''
    }

    constructor(private componentService: ComponentHolderService,
        private eventService: EventService,
        private route: ActivatedRoute,
        private router: Router,
        @Inject(ROUTES) private routes: Routes) {

    }

    @HostListener("keypress", ["$event"])
    public onClick(event: KeyboardEvent): void {
        if (event.keyCode === 13) {
            this.manageSearch();
        }
    }


    manageSearch(): void {
        let filterHeader = this.componentService.getHeaderComponent('currentSearchHeader').getHeaderFilter();
        this.componentService.getMiddleComponent('currentSearchTable').triggerSubmit(filterHeader);
    }

    resetFilter(): void {
        this.componentService.getHeaderComponent('currentSearchHeader').resetFilter();
    }

    ngOnInit(): void {
        let initRoute = null;
        //console.log('ngOnInit CommonHeaderComponent: ' + this.router.url);
        //  console.log('ngOnInit Root: ' + this.rootComponent)
        let rootComponent = this.rootComponent;

        let root = this.routes.filter((r) => r.component === rootComponent);
        root[0].children.forEach((c) => {

            if (this.router.url.match(c.path) && c.path.length > 0) {
                //console.log('children path is: ' + c.path);
                //console.log(c);

                this.contentForm = c.data.headerComponent;
                this.contentAll = c.data.headerComponent;
                this.headerTitle = c.data.headerTitle;
                this.headerIcon = c.data.headerIcon;
                this.showHeaderHook = c.data.showHeaderHook;
                this.isForSearch = c.data.isForSearch;
            }
        })




        this.eventService.eventEmitter.subscribe((ev) => {
            //console.log('received event from: ' + ev.from + ' - with payload: ' + ev.data);
            this.inputs.currentBookingId = ev.data;
        })

        this.router.events
            .filter(event => event instanceof NavigationEnd)
            .subscribe(event => {
                //console.log('router event is: ' + event);
                let currentRoute = this.route;
                let currentRoutePath = this.router.url.split('/')[1];
                //console.log('current route from common header is: ' + currentRoutePath);
                // "ricerca-prenotazioni"
                do {
                    let childrenRoutes = currentRoute.children;
                    currentRoute = null;
                    let currentPath = '';
                    childrenRoutes.forEach(route => {
                        if (route.outlet === 'primary') {
                            let routeSnapshot = route.snapshot;
                            currentPath = routeSnapshot.url.map(segment => segment.path)[0];

                            //console.log('Current currentPath is: ' + currentPath);
                            //console.log('Current currentRoutePath is: ' + currentRoutePath);
                            //console.log('Current header component is' + route.snapshot.data.headerComponent);
                            if (currentRoutePath === '' + currentPath && route.snapshot.data.headerComponent) {
                                this.contentForm = route.snapshot.data.headerComponent;
                                this.contentAll = route.snapshot.data.headerComponent;
                                this.headerTitle = route.snapshot.data.headerTitle;
                                this.headerIcon = route.snapshot.data.headerIcon;
                                this.showHeaderHook = route.snapshot.data.showHeaderHook;
                                this.isForSearch = route.snapshot.data.isForSearch;
                            } else {
                                this.contentForm = null;
                            }
                            currentRoute = route;
                        }
                    })
                } while (currentRoute);
            });
    }
}
