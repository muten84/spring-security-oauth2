import { Inject, Injectable, Optional } from '@angular/core';
import { Http, Headers, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Subscriber } from 'rxjs/Subscriber';
import { Subscription } from 'rxjs/Subscription';

interface StringSubscriberMap {
    [topic: string]: Subscriber<Event>[];
}

interface TopicObservableMap {
    [topic: string]: Observable<Event>;
}

export interface Event {
    type?: string;
    payload?: any;
}

@Injectable()
export class LocalBusService {
    //private globalObservable: Observable<Event>;
    // private observers: Subscriber<string>[] = [];

    private map: StringSubscriberMap = {};
    private observableMap: TopicObservableMap = {};


    public notifyAll(topic: string, event: Event) {
        const subs: Subscriber<Event>[] = this.map[topic];
        if (!subs) {
            return;
        }
        subs.forEach((o) => o.next(event));
    }

    public addObserver(topic: string): Observable<Event> {
        let observable = this.observableMap[topic];
        if (!observable) {
            observable = new Observable<Event>((observer: Subscriber<Event>) => {
                let subs: Subscriber<Event>[] = this.map[topic];
                if (!subs) {
                    subs = [];
                }
                subs.push(observer);
                this.map[topic] = subs;
            });
        }
        return observable;
    }

    public removeAllObservers(topic: string, subscriptions?: Array<Subscription>) {
        if (subscriptions) {
            subscriptions.forEach((s) => {
                s.unsubscribe();
            })
        }
        let subs: Subscriber<Event>[] = this.map[topic];
        if (!subs) {
            return;
        }
        subs = undefined;
        this.map[topic] = [];

    }
}
