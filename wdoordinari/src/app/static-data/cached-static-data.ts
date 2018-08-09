import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';

import { Observable, BehaviorSubject, Subject } from 'rxjs/Rx';
import { BackendConfigService } from '../service/config.service';
import 'rxjs/Rx';


import {
    CoreLayerModuleServiceService,
    Value
} from '../../services/services.module'
import { ComponentHolderService } from 'app/service/shared-service';


const STATIC_DATA_KEY = 'staticDataMap';
const TS_CONFIGURATION = 'tsConfigurationMap';
const C118_CONFIGURATION = '118ConfigurationMap';
@Injectable()
export class StaticDataService {

    private refreshing = false;

    private observables: {
        [key: string]: BehaviorSubject<Array<Value>>;
    };

    constructor(
        private staticDataService: CoreLayerModuleServiceService,
        private componentService: ComponentHolderService
    ) {
        this.observables = {};
    }

    getStaticData(type: string): Array<Value> {
        let staticDataMap = localStorage.getItem(STATIC_DATA_KEY);
        staticDataMap = JSON.parse(staticDataMap);
        return staticDataMap[type];
    }

    getStaticDataByType(type: string): Observable<Array<Value>> {
        let toRet = this.observables[type];
        if (toRet == null) {
            toRet = new BehaviorSubject<Array<Value>>([]);
            this.reloadStaticDataByType(type, toRet);
            this.observables[type] = toRet;
        }
        return toRet;
    }


    reloadStaticDataByType(type: string, subj: BehaviorSubject<Array<Value>>) {
        let staticDataMap = localStorage.getItem(STATIC_DATA_KEY);
        if (!staticDataMap) {
            this.refreshStaticData();
        } else {
            staticDataMap = JSON.parse(staticDataMap);
            setTimeout(() =>
                subj.next(staticDataMap[type])
                , 100);
        }
    }

    getConfigValueByKey(key: string, def: string) {
        let staticDataMap = localStorage.getItem(STATIC_DATA_KEY);
        let ret = undefined;
        if (staticDataMap && staticDataMap != null) {
            staticDataMap = JSON.parse(staticDataMap);
            if (staticDataMap['CONFIG_FE'] != null) {
                ret = staticDataMap['CONFIG_FE'].filter(x => x.name == key);//[key];
            }
        }
        if (ret && ret.length > 0)
            return ret[0].description;
        else
            return def;
    }

    getTSConfig(key: string, def: string) {
        let configMap = localStorage.getItem(TS_CONFIGURATION);
        if (configMap) {
            configMap = JSON.parse(configMap);
            let config = configMap[key];
            if (config) {
                return config.description;
            }
        }
        return def;
    }

    get118Config(key: string, def: string) {
        let configMap = localStorage.getItem(C118_CONFIGURATION);
        if (configMap) {
            configMap = JSON.parse(configMap);
            let config = configMap[key];
            if (config) {
                return config.description;
            }
        }
        return def;
    }


    //    loadStaticData(): Observable<Array<Value>> {
    //        let subj = new BehaviorSubject<Array<Value>>( [] );
    //
    //        let staticDataMap = localStorage.getItem( STATIC_DATA_KEY );
    //        if ( !staticDataMap ) {
    //            this.refreshStaticData().subscribe(( res ) => subj.next( res ) );
    //        } else {
    //            subj.next( JSON.parse( staticDataMap ) );
    //        }
    //        return subj;
    //    }

    reloadStaticData(): Observable<any> {
        let subj = new BehaviorSubject<any>([]);
        setTimeout(() => subj.next({ result: true }), 100);
        localStorage.removeItem(STATIC_DATA_KEY);
        return subj;
    }


    public async refreshStaticDataIfEmpty() {
        let staticDataMap = localStorage.getItem(STATIC_DATA_KEY);
        if (!staticDataMap) {
            await this.refreshStaticData();
        }
    }


    public async refreshStaticData() {
        if (!this.refreshing) {
            this.refreshing = true;

            let root = this.componentService.getRootComponent();
            if (root) {
                root.mask();
            }
            // carico i dati dal server 
            let values: { [key: string]: Array<Value>; } = await this.staticDataService.loadStaticData_1().toPromise();

            localStorage.setItem(STATIC_DATA_KEY, JSON.stringify(values));
            for (let key in values) {
                if (values[key]) {
                    if (!this.observables[key]) {
                        this.observables[key] = new BehaviorSubject<Array<Value>>(values[key]);
                    } else {
                        this.observables[key].next(values[key]);
                    }
                }
            }
            let confs: { [key: string]: Value; } = await this.staticDataService.getAllTSConfigurations().toPromise();
            localStorage.setItem(TS_CONFIGURATION, JSON.stringify(confs));

            let confs118: { [key: string]: Value; } = await this.staticDataService.getAll118Configurations().toPromise();
            localStorage.setItem(C118_CONFIGURATION, JSON.stringify(confs118));

            if (root) {
                root.unmask();
            }
            this.refreshing = false;
        }
    }
}
