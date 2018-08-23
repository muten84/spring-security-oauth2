import { Injectable } from "@angular/core";
import { Resolve } from "@angular/router";
import { StaticDataService } from "./cached-static-data";

@Injectable()
export class StaticDataResolve implements Resolve<any> {

    constructor(private service: StaticDataService) { }

    async resolve(route: any) {
        return await this.service.refreshStaticDataIfEmpty();
    }
}