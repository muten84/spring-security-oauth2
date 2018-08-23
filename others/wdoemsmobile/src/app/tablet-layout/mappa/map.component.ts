import { Component, OnInit } from '@angular/core';
import { slideToRight } from '../../router.animations';
import { ConfigurationService } from '../../service/core/configuration.service';


@Component({
    selector: 'app-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.scss'],
    animations: [slideToRight()]
})
export class MapComponent implements OnInit {

    disableAnimation;

    constructor(private configService: ConfigurationService) { }

    ngOnInit() {
        let enableAnimation: boolean = this.configService.getConfigurationParam("GENERAL", "ENABLE_ANIMATION", true);
        this.disableAnimation = !enableAnimation;
    }
}
