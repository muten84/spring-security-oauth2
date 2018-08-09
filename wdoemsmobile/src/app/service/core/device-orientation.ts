import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { Observer } from "rxjs/Observer";
import { Message } from '../service.module';

export function handleOrientation(event) {
    //console.log("orientation event: " + event);
}

@Injectable()
export class DeviceOrientationService {

    handle = handleOrientation;

    constructor() {
        //console.log("DEVICE ORIENTATION init ");
        setTimeout(() => {
            this.create().subscribe((event) => {
                //console.log("device orientation event");
            });
        }, 1000);

    }

    private animateLogo(LR, FB, DIR) {
        //for webkit browser
        /*rotate3d(1,0,0, " + (FB * -1) + "deg)*/
        let logo = document.getElementById("imggLogo");
        //console.log("rotate: ", logo, LR, FB, DIR);
        if (logo) {

            /* logo.style.webkitTransform = "rotate(" + LR + "deg) rotate3d(1,0,0, " + (FB * -1) + "deg)"; */
            logo.style.webkitTransform = "rotate(" + LR + "deg)";
        }

        //for HTML5 standard-compliance
        //document.getElementById("imgLogo").style.transform = "rotate(" + LR + "deg) rotate3d(1,0,0, " + (FB * -1) + "deg)";
    }


    public create(): Subject<MessageEvent> {
        //console.log("DEVICE ORIENTATION create ");
        let observable = Observable.create(
            (obs: Observer<MessageEvent>) => {

                return () => {
                    //console.log("orientation service closed");
                }
            });
        let observer = {
            next: (data: DeviceOrientationEvent) => {
                //console.log("data: " + data.gamma + " - " + data.beta);
                let L = data.gamma;
                let F = data.beta;
                let D = data.alpha;

                this.animateLogo(L, F, F);
            }
        };
        window.addEventListener('deviceorientation', (event: DeviceOrientationEvent) => {
            ////console.log("event orientation: " + event);
            observer.next(event);
        });
        return Subject.create(observer, observable);
    }
}