import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Subject } from 'rxjs';
import { AddressBookingDTO, AuthorityDTO, AuthorityModuleServiceService, CoreLayerModuleServiceService, Department, FullTextResult } from 'services/gen';
import { SelectComponent } from '../core/select/select';
import { ComponentHolderService } from '../service/shared-service';

@Component({
    selector: 'fulltext',
    templateUrl: './fulltext-component.html'
})
export class FullTextComponent implements OnInit {

    fulltextValue: FullTextResult;
    searchedValue: string;
    protected _model: AddressBookingDTO;

    @ViewChild('selectFullText') selectFullText: SelectComponent;

    get address(): AddressBookingDTO {
        if (!this._model) {
            this._model = <AddressBookingDTO>{};
        }
        return this._model;
    }

    public focus() {
        this.selectFullText.inputMode = true;
        this.selectFullText.focusToInput();
    }

    @Input('address') set address(model: AddressBookingDTO) {
        this._model = JSON.parse(JSON.stringify(model));
    }


    @Output('addressChange') addressChange: EventEmitter<AddressBookingDTO> = new EventEmitter<AddressBookingDTO>();

    departmentFullTextList: Subject<Array<any>>;

    constructor(
        private coreService: CoreLayerModuleServiceService,
        private componentService: ComponentHolderService,
        private authorityService: AuthorityModuleServiceService,
    ) {
    }

    ngOnInit() {
        this.departmentFullTextList = new Subject();
    }

    updateDepartmentFullText(fulltext?: string) {
        this.searchedValue = fulltext;
        this.coreService.searchFullText(fulltext).subscribe(list => {
            this.departmentFullTextList.next(list.map(el => {
                let newEl: any = Object.assign({}, el);
                newEl.text = el.authority + (el.department ? el.department : '');
                return newEl;
            }));
        });
    }

    rebuildIndex() {
        this.coreService.rebuiltFullText().subscribe(list => {

        });
    }

    public async setDepartmentOrAuthority(result: FullTextResult) {
        this.componentService.getRootComponent().mask();

        let dep: Department;
        let auth: AuthorityDTO;
        this.address.costCenter = undefined;
        if (result.type === 'DEPARTMENT') {
            // carico il reparto
            dep = await this.coreService.departmentById(result.id).toPromise();
            let body = { id: result.id };
            // carico pure l'authority
            auth = await this.authorityService.searchAuthorityByDepId(body).toPromise();

        } else if (result.type === 'AUTHORITY') {
            let body = { id: result.id };
            // carico l'authority
            auth = await this.authorityService.searchAuthorityById(body).toPromise();
        }

        this.address.department = dep;
        this.address.authority = auth;


        if (auth) {
            if (auth.address) {
                this.address.province = auth.address.province;
                this.address.street = auth.address.street;
                this.address.municipality = auth.address.municipality;
                this.address.locality = auth.address.locality;
                this.address.civicNumber = auth.address.civicNumber;
            }


            if (result.type === 'AUTHORITY') {
                if (auth.costCenter)
                    this.address.costCenter = auth.costCenter;
                this.address.telephones = auth.phoneNumbers;
                if (auth.phoneNumbers && auth.phoneNumbers.length > 0)
                    this.address.phoneNumber = auth.phoneNumbers[0];
            }
        }


        if (dep) {
            if (dep.province) {
                this.address.province = { name: dep.province };
            }
            if (dep.streetName) {
                this.address.street = { name: dep.streetName };
            }
            if (dep.municipality) {
                this.address.municipality = { name: dep.municipality };
            }
            if (dep.locality) {
                this.address.locality = { name: dep.locality };
            }

            this.address.civicNumber = dep.houseNumber;
            if (result.type === 'DEPARTMENT') {
                if (dep.phoneNumbers && dep.phoneNumbers) {
                    let tels = dep.phoneNumbers.filter(x => x.indexOf(this.searchedValue) > -1);
                    if (tels && tels.length > 0) {
                        this.address.phoneNumber = tels[0];
                    } else {
                        this.address.phoneNumber = dep.phoneNumbers[0];
                    }
                }

                this.address.telephones = dep.phoneNumbers;
                if (dep.costCenter)
                    this.address.costCenter = dep.costCenter;
                else
                    this.address.costCenter = auth.costCenter;
            }
            //     this.detail = (result.pavilionName ? result.pavilionName : '') + (result.pavilionNumber ? ' ' + result.pavilionNumber : '');
        }

        // pulisco la combobox
        this.fulltextValue = null;
        this.departmentFullTextList.next([]);
        this.componentService.getRootComponent().unmask();

        // invio l'evento di modifica
        this.addressChange.emit(this.address);
        //this.componentService.getRootComponent().unmask();
    }
}