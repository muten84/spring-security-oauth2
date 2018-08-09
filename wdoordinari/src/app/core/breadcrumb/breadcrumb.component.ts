import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, ActivatedRouteSnapshot, NavigationEnd, Params, Router } from "@angular/router";
import { Observable } from "rxjs";
import "rxjs/add/operator/filter";

interface BreadCrumb {
  label: string;
  url: string;
  params?: Params;
  queryParams?: Params;
}

@Component({
  selector: "breadcrumb",
  template: `
  <ul class="breadcrumb" >    
      <li  class="breadcrumb-item" *ngFor="let route of breadcrumbs.list; let i = index" >
      <a (click)="goTo(route, i)">{{ route.label }}</a>
      </li>
  </ul>
  `,
  styleUrls: ['./breadcrumb.component.scss']
})
export class BreadcrumbComponent implements OnInit {

  breadcrumbs: RingBuffer<BreadCrumb>;


  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.breadcrumbs = new RingBuffer<BreadCrumb>(5);

    this.router.events
      .filter(event => event instanceof NavigationEnd)
      .subscribe((event: NavigationEnd) => {
        /*
         console.log(this.route.snapshot.data);
         console.log(this.router.routerState.snapshot);
         console.log(event);
 */
        let label = this.getLabel(this.router.routerState.snapshot.root);
        // mi carico i parametri passati, in modo da riusare gli stessi se si clicca sulla breadcrumb
        Observable.combineLatest(this.route.params, this.route.queryParams,
          (params, qparams) => ({ params, qparams })).subscribe(ap => {
            //  console.log('params' + JSON.stringify(ap.params));
            //  console.log('qparams' + JSON.stringify(ap.qparams));
            let parLength = Object.keys(ap.params).length;
            let qparLength = Object.keys(ap.qparams).length;

            if (this.breadcrumbs.length === 0 || parLength !== 0 || qparLength !== 0 || event.url !== this.breadcrumbs.last().url) {
              this.breadcrumbs.push({
                label: label,
                url: event.url,
                params: ap.params,
                queryParams: ap.qparams,
              });
            }
          });
      });
  }

  goTo(b: BreadCrumb, index: number) {
    if ((index + 1) != this.breadcrumbs.list.length) {
      this.router.navigate([b.url], { queryParams: b.queryParams });
    }
  }

  getLabel(root: ActivatedRouteSnapshot): string {
    if (root.children.length === 0) {
      if (typeof root.data.breadcrumb === 'function') {
        return root.data.breadcrumb();
      } else {
        return root.data.breadcrumb;
      }
    } else {
      return this.getLabel(root.children[0]);
    }
  }
}

export class RingBuffer<T>  {

  private _size: number;

  private buffer: Array<T> = [];

  constructor(size: number) {
    this._size = size;
  }

  public get size(): number {
    return this._size;
  }

  public get length(): number {
    return this.buffer.length;
  }

  public get list(): Array<T> {
    return this.buffer;
  }

  public last(): T {
    return this.buffer[this.buffer.length - 1];
  }

  private shiftBack(length: number) {
    var overwrite = (this.length + length) - this.size;

    if (overwrite > 0) {
      this.buffer.splice(0, overwrite);
    }
  }

  private shiftFor(length: number) {
    var overwrite = (this.length + length) - this.size;

    if (overwrite > 0) {
      var startAt = this.length - overwrite;
      this.buffer.splice(startAt, overwrite);
    }
  }

  push(...items: T[]): number {
    this.shiftBack(items.length);
    return this.buffer.push(...items);
  }

  /**
    * Combines two or more arrays.
    * @param items Additional items to add to the end of array1.
    */
  concat<U extends T[]>(...items: U[]): T[];
  /**
    * Combines two or more arrays.
    * @param items Additional items to add to the end of array1.
    */
  concat(...items: T[]): T[] {
    this.shiftBack(items.length);
    return this.buffer.concat(items);
  }

  /**
    * Removes elements from an array and, if necessary, 
    * inserts new elements in their place, returning the deleted elements.
    * @param start The zero-based location in the array from which 
                * to start removing elements.
    * @param deleteCount The number of elements to remove.
    * @param items Elements to insert into the array in place of the deleted elements.
    */
  splice(start: number, deleteCount?: number, ...items: T[]): T[] {
    var removed = this.buffer.splice(start, deleteCount);
    this.push(...items);
    return removed;
  }

  /**
    * Inserts new elements at the start of an array.
    * @param items  Elements to insert at the start of the Array.
    */
  unshift(...items: T[]): number {
    this.shiftFor(items.length);
    return this.buffer.unshift(...items);
  }
}