import { Component, Input } from '@angular/core';
import { AddressBookingDTO, ReqBookingDTO, AuthorityInfoDTO } from 'services/services.module';
import { FormGroupGeneratorService } from 'app/core/validation/validation.module';
import { FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs/Rx';
import { ComponentHolderService } from 'app/service/shared-service';
import { AuthorityModuleServiceService, CoreLayerModuleServiceService, AuthorityFilterDTO, AuthorityDTO, Department, DepartmentInfoDTO, DepartmentFilter } from 'services/services.module';
import { ResultGridModalComponent } from 'app/gestione-prenotazioni/result-grid-modal-component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { StaticDataService } from 'app/static-data/cached-static-data';
import { valueToSelect } from 'app/util/convert';
import * as _ from 'lodash';

@Component({
    selector: 'richiedente',
    templateUrl: './richiedente.component.html'
})
export class RichiedenteComponent {

    @Input() model: ReqBookingDTO;
    @Input() convenzioneItems: Observable<Array<any>>;

    //@Input() departmentList: Observable<Array<any>>;

    /* FormGroup per la validazione */
    prenotazioniFG: FormGroup;
    departmentList: Subject<Array<any>>;
    authorityList: Subject<Array<any>>;
    authorityTypeList: Observable<Array<any>>;
    startTelephoneList: Observable<Array<any>>;

    constructor(
        private modalService: NgbModal,
        private componentService: ComponentHolderService,
        private authorityService: AuthorityModuleServiceService,
        private staticDataService: StaticDataService,
        private coreService: CoreLayerModuleServiceService,
        private fgs: FormGroupGeneratorService,
    ) {
        this.prenotazioniFG = this.fgs.getFormGroup('prenotazioni');
    }

    ngOnInit(): void {

        this.authorityTypeList = this.staticDataService.getStaticDataByType('AUTHORITY_TYPE').map(valueToSelect);
        this.departmentList = new Subject();
        this.authorityList = new Subject();
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.prenotazioniFG);
    }

    searchAuthority(cc: string, telephone: string) {
        if (cc) {
            this.componentService.getRootComponent().mask();
            let body = { costCenter: cc };
            this.authorityService.searchAuthorityByCostCenter(body).catch((e, o) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(value => {
                this.componentService.getRootComponent().unmask();
                this.setAuthorities(value);
            });
        }

        if (telephone) {
            this.componentService.getRootComponent().mask();
            let body = { telephone: telephone };
            this.authorityService.searchAuthorityByTelephone(body).catch((e, o) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(value => {
                this.componentService.getRootComponent().unmask();
                this.setAuthorities(value);
            });
        }
    }

    setAuthorities(value: Array<AuthorityInfoDTO>) {

        let idx = 0;
        let valueList: Array<AuthorityInfoDTO> = [];
        if (value != null && value.length > 0) {
            for (idx = 0; idx < value.length; idx++) {
                let ext: AuthorityInfoDTOExt = value[idx];
                if (ext.telephones != null && ext.telephones.length > 0) {
                    ext.telephoneList = ext.telephones.toString().split(",").join("<br />")
                }
                valueList.push(ext);
            }
        }

        value = valueList;

        //console.log(value);
        if (value && value.length === 1) {

            let body = { id: value[0].id };
            this.authorityService.searchAuthorityById(body).subscribe(value => {
                if (value) {
                    this.setAuthority(value);
                }
            });
            return;
        }

        //Ordina per compact value
        value = _.sortBy(value, 'compact');
        
        let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });

        modal.componentInstance.list = value;
        modal.componentInstance.title = 'Enti';
        modal.componentInstance.columns = [
            { title: 'Tipo Ente', name: 'type', style: { width: "20%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Descrizione', name: 'compact', style: { width: "50%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Numeri di telefono', name: 'telephoneList', style: { width: "25%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } }
        ];

        modal.result.then((result) => {
            //console.log("modal result is: " + result);
            if (result) {
                let body = { id: result.id };
                this.authorityService.searchAuthorityById(body).subscribe(value => {
                    if (value) {
                        this.setAuthority(value);

                        //this.departmentList.next(auth.departments);
                        this.updateDepartmentList();
                    }
                });

            }
        }, (reason) => {
        });


        this.componentService.getRootComponent().unmask();

    }

    setAuthority(auth: AuthorityDTO) {

        this.model.authority = { description: auth.description };
        this.model.description = auth.description;
        this.model.type = auth.type;
        this.model.cc = auth.costCenter;
        this.model.vat = auth.vat;
        if (auth.phoneNumbers && auth.phoneNumbers.length > 0) {
            this.model.telephone = auth.phoneNumbers[0];
        }
        this.model.convention = auth.convention;
        this.model.department = { description: '' };

        /* controlla */
        this.departmentList.next(auth.departments);       

        this.componentService.getRootComponent().unmask();

    }

    searchDepartmentByCC(cc: string) {
        if (!cc || cc.length < 1) {
            this.componentService.getRootComponent().showModal('Ricerca per centro di costo', 'Inserisci almeno un carattere');
            return;
        }
        if (cc) {
            let body = { costCenter: cc };
            this.componentService.getRootComponent().mask();
            this.authorityService.searchDepartmentsByCenterCost(body).catch((err, c) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(value => {
                if (value.length > 0) {
                    this.setDepartments(value);
                } else {
                    //this.authorityService.searchAuthorityByCostCenter(body).subscribe(value => {     });
                    this.searchAuthority(cc, null);
                }
                //this.componentService.getRootComponent().unmask();
            });
        }
    }

    setDepartments(value: Array<DepartmentInfoDTO>) {
        this.componentService.getRootComponent().mask();
        if (value && value.length === 1) {
            this.setDepartmentAndAuthority(value[0]);
            return;
        }

        let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });
        //Ordina elenco dipartimenti per compact name
        value = _.sortBy(value, ['compact', 'description'])
        modal.componentInstance.list = value;
        modal.componentInstance.title = 'Reparti';
        modal.componentInstance.columns = [
            { title: 'Tipo Ente', name: 'autorityType', style: { width: "15%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Descrizione', name: 'authorityCompact', style: { width: "25%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Reparto', name: 'description', style: { width: "15%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Indirizzo', name: 'address', style: { width: "15%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Centro di Costo', name: 'costCenter', style: { width: "15%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Numeri di telefono', name: 'phoneNumbers', style: { width: "15%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } }
        ];

        modal.result.then((result) => {
            //console.log("modal result is: " + result);
            if (result) {
                this.setDepartmentAndAuthority(result);
            }
        }, (reason) => {
            console.log("modal result error ");
        });
    }

    setDepartmentAndAuthority(result: DepartmentInfoDTO) {
        this.componentService.getRootComponent().mask();
        //salvo valore del telefono attualemnte settato per la ricerca
        let phoneNumberSearch = this.model.telephone;
        let body = { id: result.id };
        //cerco l'autority legata al department
        this.authorityService.searchAuthorityByDepId(body).subscribe(auth => {
            this.componentService.getRootComponent().mask();
            this.setAuthority(auth);
            //sovrascrivo il centro di costo con quello del reparto
            this.model.cc = result.costCenter;
            //nel caso non sia presente cc del dip si setta quello della authority
            if(!result.costCenter)
                this.model.cc= auth.costCenter;
            this.setDepartment(result);

            //valorizzo telefono
            if (result.phoneNumbers && result.phoneNumbers.length > 0) {
                let telephones = result.phoneNumbers;
                let tel = result.phoneNumbers[0];
                let telSearch = result.phoneNumbers.filter(x => x.indexOf(phoneNumberSearch) > -1);
                if (telSearch.length > 0) tel = telSearch[0];
                this.model.telephone = tel;
            } else if (auth.phoneNumbers && auth.phoneNumbers.length > 0) {
                this.model.telephone = auth.phoneNumbers[0];
            }

            this.componentService.getRootComponent().unmask();
        });

        //this.componentService.getRootComponent().unmask();
    }

    setDepartment(dep: DepartmentInfoDTO) {

        if (dep) {
            this.model.department = {
                id: dep.id,
                name: dep.compact,
                description: dep.description,
                compactDescription: dep.compact

            }
            this.model.detail = dep.description;
        }
    }

    updateAuthorityList(name?: string) {

        //console.log('updateAutorityList ');

        let filter: AuthorityFilterDTO = {};

        if (name) {
            filter.description = name.toUpperCase();
        }

        if (this.model.type) {
            filter.type = this.model.type;
        }

        filter.withPhone = true;
        this.authorityService.searchAuthorityByFilter(filter).catch((err, c) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(list => {
            if (list && list.length > 0) {
                this.departmentList = new Subject();
            }
            this.authorityList.next(list);
            this.componentService.getRootComponent().unmask();
        });
        
    }

    updateDepartmentList(name?: string) {
        // TODO aggiungere ricerca reparto

        let filter: DepartmentFilter = {
            descrizione: name ? name.toUpperCase() : undefined,
            nomeEnte: this.model.authority ? this.model.authority.description : undefined,
            tipoEnte: this.model.type ? this.model.type : undefined,
            convenzione: this.model.convention ? this.model.convention : undefined
        };

        this.coreService.departmentByFilter(filter).subscribe(list => {
            //console.log(list);
            this.departmentList.next(list);
            this.componentService.getRootComponent().unmask();
        });
    }

    searchDepartmentByPhone(phone: string) {
        if (!phone || phone.length < 2) {
            this.componentService.getRootComponent().showModal('Ricerca per telefono', 'Inserisci almeno due cifre');
            return;
        }

        if (phone) {

            let body = { telephone: phone };
            this.componentService.getRootComponent().mask();
            this.authorityService.searchDepartmentByTelephone(body).catch((err, c) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(value => {
                if (value.length > 0) {
                    this.setDepartments(value);
                } else {
                    //this.authorityService.searchAuthorityByCostCenter(body).subscribe(value => {     });
                    this.searchAuthority(null, phone);
                }
                this.componentService.getRootComponent().unmask();
            });
        }
    }
}

interface AuthorityInfoDTOExt extends AuthorityInfoDTO {

    telephoneList?: string;
}
