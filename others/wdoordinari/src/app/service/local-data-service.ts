import { Injectable } from "@angular/core";


export type BeanName = 'GESTONE_SERVIZI_CHECK';

@Injectable()
export class LocalDataService {


    public getBean<T>(beanName: BeanName, defaultValue: T): T {
        let stringValue = localStorage.getItem(beanName);
        let target;
        if (stringValue) {
            target = JSON.parse(stringValue);
        } else {
            target = defaultValue;
        }
        // creo un proxy per intercettare gli eventi di modifica al bean 
        return new Proxy(target, {
            set: (target: T, p: PropertyKey, value: any, receiver: any): boolean => {
                target[p] = value;

                let stringVal = JSON.stringify(target);
                localStorage.setItem(beanName, stringVal);
                return true;
            }
        });
    }


}