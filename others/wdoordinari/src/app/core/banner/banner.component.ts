import { ChangeDetectionStrategy, ViewChild, ElementRef, Directive, Component, OnInit, ViewEncapsulation, ViewContainerRef, EventEmitter, Input, Output } from '@angular/core';
import { BannerModel } from './banner-model';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
    selector: 'banner',
    templateUrl: './banner.component.html',
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,

})
export class CommonBannerComponent implements OnInit {

    @Input()
    bannerModel: BannerModel;

    ngOnInit(): void {

    }

    public constructor(private sanitizer: DomSanitizer) {

    }

    onCurrentBookingCode(event: any) {
        //console.log("onCurrentBookingCode");
    }


    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    public getData(html) {
        return this.sanitize(html);
    }


}