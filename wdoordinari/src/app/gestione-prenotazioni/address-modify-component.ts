
import { Component, Input, OnInit, EventEmitter, Output, HostListener, ElementRef } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroupGeneratorService } from 'app/core/validation/validation.module';
import { FormGroup } from '@angular/forms';
import { Observable, Subject } from 'rxjs/Rx';
import * as _ from 'lodash';
import {
    AuthorityModuleServiceService,
    AddressBookingDTO, AuthorityFilterDTO, DepartmentInfoDTO, AuthorityInfoDTO,
    Value, AuthorityDTO, CoreLayerModuleServiceService, DepartmentFilter, Department, StreetMapServiceService, StreetFilterDTO
} from 'services/services.module';
import { StaticDataService } from 'app/static-data/cached-static-data';
import { valueToSelect } from 'app/util/convert';
import { ResultGridModalComponent } from 'app/gestione-prenotazioni/result-grid-modal-component';
import { ComponentHolderService } from 'app/service/shared-service';
import { ProvinceFilterByMunicipalityDTO, LocalityFilterDTO } from 'services/gen';

@Component({
    selector: 'address-modify',
    templateUrl: './address-modify-component.html'
})
export class AddressModifyComponent implements OnInit {

    @Output() public exit: EventEmitter<any> = new EventEmitter();
    @Output() public save: EventEmitter<any> = new EventEmitter();

    public authorityType;
    public convention;
    //protected cc: string;
    public detail: string;
    public streetName: string;

    filterLocality: LocalityFilterDTO;


    protected _model: AddressBookingDTO;
    private modelBck: any;
    public phoneNumberSelected;

    // TODO implementare get e set
    get model(): AddressBookingDTO {
        if (!this._model) {
            this._model = <AddressBookingDTO>{};
        }
        return this._model;
    }

    @Input('address') set model(model: AddressBookingDTO) {
        this._model = JSON.parse(JSON.stringify(model));
        this.setOtherValue(this._model.authority);
        this.saveBckValues(this._model);
    }


    @Input('type') type: 'Start' | 'End';

    /* FormGroup per la validazione */
    @Input('fg') prenotazioniFG: FormGroup;

    conventionList: Observable<Array<any>>;
    authorityTypeList: Observable<Array<any>>;
    authorityList: Subject<Array<any>>;
    departmentList: Subject<Array<any>>;
    municipalityList: Subject<Array<any>>;
    localityList: Subject<Array<any>>;
    provinceList: Subject<Array<any>>;
    //telephoneList: Array<String>;

    constructor(
        private modalService: NgbModal,
        private componentService: ComponentHolderService,
        private authorityService: AuthorityModuleServiceService,
        private staticDataService: StaticDataService,
        private coreService: CoreLayerModuleServiceService,
        private streetService: StreetMapServiceService,
        private fgs: FormGroupGeneratorService,
        private el: ElementRef
    ) {


    }

    ngOnInit(): void {

        this.authorityTypeList = this.staticDataService.getStaticDataByType('AUTHORITY_TYPE').map(valueToSelect);
        this.conventionList = this.staticDataService.getStaticDataByType('CONVENTION').map(valueToSelect);

        this.authorityList = new Subject();
        this.departmentList = new Subject();
        this.municipalityList = new Subject();
        this.localityList = new Subject();
        this.provinceList = new Subject();
        //this.telephoneList = [];
    }

    saveBckValues(model: AddressBookingDTO) {
        if (model.municipality != null) {
            this.modelBck = _.cloneDeep(model);
            this.modelBck.authorityType = this.authorityType;
            this.modelBck.convention = this.convention;
            this.modelBck.detail = this.detail;
            this.modelBck.streetName = this.streetName;
            //this.modelBck.telephoneList = this.telephoneList;
            this.modelBck.phoneNumberSelected = this.phoneNumberSelected;
            this.modelBck.phoneNumber = this.model.phoneNumber;
            this.modelBck.costCenter = this.model.costCenter;
        }
    }

    getControl(name: string) {
        return this.fgs.getControl(name, this.prenotazioniFG);
    }

    private checkTelephoneValid() {
        let check = ((!this.model.phoneNumber && (!this.model.telephones || this.model.telephones.length == 0))
            ||
            (this.model.phoneNumber && this.model.telephones.indexOf(this.model.phoneNumber) > -1));
        /*( (!this.model.phoneNumber && !this.telephoneList && this.telephoneList.length==0)
         ||
         (this.model.phoneNumber && this.telephoneList.indexOf(this.model.phoneNumber)>-1));*/
        return check;
    }

    private checkCostCenterValid() {
        let check =
            (_.isEmpty(this.model.costCenter) &&
                !this.model.authority.costCenter &&
                (_.isEmpty(this.model.department) || !this.model.department.costCenter)
                ||
                !_.isEmpty(this.model.costCenter) &&
                ((this.model.authority.costCenter == this.model.costCenter && (_.isEmpty(this.model.department) || _.isEmpty(this.model.department.costCenter) ))
                    || (!_.isEmpty(this.model.department) && this.model.department.costCenter == this.model.costCenter)
                ));
        return check;
    }

    close(): boolean {
        this.setStreetfromValue();
        //se non ho settato il compound address perchè non ho l'authority, lo setto
        if (!this.model.authority) {
            this.model.compoundAddress = this.getCompoundAddress(this.model.authority);
        } else {
            let messageError = [];
            //Controllo valore valido telefono authority
            if (!this.checkTelephoneValid()) {
                let numeroInserito = this.model.phoneNumber;
                messageError.push("Il telefono '" + numeroInserito + "' inserito non è valido per l'ente selezionato.");
                //this.model.phoneNumber=this.modelBck?this.modelBck.phoneNumber:undefined;        
            }

            //Controllo valore valido centro di costo authority/reparto
            if (!this.checkCostCenterValid()) {
                let centroDiCostoInserito = this.model.costCenter;
                messageError.push("Il centro di costo '" + centroDiCostoInserito + "' inserito non è valido per l'ente/reparto selezionato.");
                //this.model.costCenter=this.modelBck?this.modelBck.costCenter:undefined;
                //this.reloadBckValues();
            }

            if (messageError && messageError.length > 0) {
                this.componentService.getRootComponent().showModal("Attenzione verificare i dati inseriti", messageError);
                return false;
            }
        }
        this.save.emit(JSON.parse(JSON.stringify(this.model)));
        return true;
    }

    exitWithoutSave() {
        this.reloadBckValues();
        this.setOtherValue(this._model.authority);
        this.exit.emit(null);
    }

    reloadBckValues() {
        if (!this.modelBck) {
            this.modelBck = <AddressBookingDTO>{
                authority: null
            };
        }
        this._model = _.cloneDeep(this.modelBck);
        this.authorityType = this.modelBck.authorityType;
        this.convention = this.modelBck.convention;
        this.detail = this.modelBck.detail;
        this.streetName = this.modelBck.streetName;
        this.setOtherValue(this._model.authority);
        this.model.costCenter = this.modelBck.costCenter;
        this.model.phoneNumber = this.modelBck.phoneNumber;
        this.phoneNumberSelected = this.modelBck.phoneNumberSelected;
    }

    clean() {
        this.authorityType = null;//new Subject();
        this.convention = null;//new Subject();

        //this.model.compactAddress = null;
        this.streetName = undefined;
        this.detail = undefined;
        //this.cc = undefined;
        //this.model.costCenter = undefined;

        this.model.authority = null;
        this.model.compoundAddress = null;

        //resetAddressModel()
        // this.model.authority=null;
        this.model.civicNumber = null;
        this.model.compactAddress = null;
        //this.model.compoundAddress=null;
        this.model.costCenter = null;
        this.model.department = null;
        this.model.locality = null;
        this.model.municipality = null;
        this.model.phoneNumber = null;
        this.phoneNumberSelected = null;
        this.model.province = null;
        this.model.street = null;

        //this.model=<AddressBookingDTO>{};

        this.departmentList = new Subject();
        this.authorityList = new Subject();
    }

    public setAuthorities(value: Array<AuthorityInfoDTO>) {

        this.componentService.getRootComponent().mask();
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
        if (value && value.length == 1) {
            let body = { id: value[0].id };
            this.authorityService.searchAuthorityById(body).subscribe(value => {
                if (value) {
                    this.setAuthority(value);
                }
            });
            //this.setAuthority(value[0]);
            return;
        }

        //Ordina per compact value
        value = _.sortBy(value, 'compact');

        let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });

        modal.componentInstance.list = value;
        modal.componentInstance.title = 'Enti';
        modal.componentInstance.columns = [
            /*{ title: 'Telefoni', name: 'telephones', style: { width: "30%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },*/
            { title: 'Tipo Ente', name: 'type', style: { width: "20%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Descrizione', name: 'compact', style: { width: "50%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } },
            { title: 'Numeri di telefono', name: 'telephoneList', style: { width: "25%", "overflow": "hidden", "text-overflow": "ellipsis", "white-space": "nowrap" } }
        ];
        this.componentService.getRootComponent().unmask();
        modal.result.then((result) => {
            //console.log("modal result is: " + result);
            if (result) {
                this.componentService.getRootComponent().mask();
                let body = { id: result.id };
                this.authorityService.searchAuthorityById(body).subscribe(value => {
                    if (value) {
                        this.setAuthority(value);
                    }
                    this.componentService.getRootComponent().unmask();
                });
                //this.setAuthority(result);
            }
        }, (reason) => {
            this.componentService.getRootComponent().unmask();
        });

        //this.componentService.getRootComponent().unmask();

    }

    public setAuthority(auth: AuthorityDTO) {
        this.model.authority = auth;
        this.model.municipality = auth.address.municipality;
        this.model.locality = auth.address.locality;
        this.model.civicNumber = auth.address.civicNumber;
        this.model.street = auth.address.street;
        this.model.province = auth.address.province;
        this.model.costCenter = auth.costCenter;
        this.model.compactAddress = this.coalesceValue(auth.address.street);
        this.streetName = this.coalesceValue(auth.address.street);
        //this.model.compoundAddress = this.getCompact(auth);
        this.model.compoundAddress = this.getCompoundAddress(auth);
        this.departmentList.next(auth.departments);
        this.setOtherValue(auth);
        //reset 
        this.model.department = undefined;
        this.detail = undefined;
        if (auth.phoneNumbers && auth.phoneNumbers.length > 0) {
            //this.telephoneList = auth.phoneNumbers;
            this.model.telephones = auth.phoneNumbers;
            this.model.phoneNumber = auth.phoneNumbers[0];
            this.phoneNumberSelected = this.model.phoneNumber;
        }

        this.componentService.getRootComponent().unmask();
    }

    protected getCompoundAddress(auth: AuthorityDTO): string {
        //--compactAddress: --> auth.description - dip.description - dip.pavillon_name : auth.street, auth.number auth.locality - auth.comune (auth.PROv)
        var authorityDescr = '';
        var depDescription = '';
        var pavillonName = '';
        var pavillonNumber = '';
        var streetName = '';
        var civicNumber = '';
        var localityName = '';
        var municipalityName = '';
        var provinceName = '';
        var dep = null;
        //se c'è il department, recupero i dati dal department, altrimenti li recupero dall'authority
        if (auth) {
            authorityDescr = auth.description;
            streetName = auth.streetName;
            civicNumber = auth.houseNumber;
            localityName = auth.locality;
            municipalityName = auth.municipality;
            provinceName = auth.province;
        }
        if (auth && auth.departments) {
            dep = auth.departments[0];
            if (dep) {
                depDescription = dep.description;
                pavillonName = dep.pavilionName;
                pavillonNumber = dep.pavilionNumber;
                streetName = dep.streetName;
                civicNumber = dep.houseNumber;
                localityName = dep.locality;
                municipalityName = dep.municipality;
                provinceName = dep.province;
            }
        }
        if (this.streetName) {
            streetName = this.streetName;
        }

        this.model.authority = auth;
        this.model.department = dep;

        if (!streetName) { //non ho la strada recuperata dall'authority o dal department
            if (this.model.street) { //ho settato la strada dal campo Indirizzo
                streetName = this.model.street.name
            }
            /*else {
                var street = {name:streetName}
                this.model.street = street;
            }*/
        }
        if (!civicNumber) {
            civicNumber = this.model.civicNumber;
        }
        if (!localityName) {
            if (this.model.locality) {//ho settato la locality dal campo Indirizzo o Località
                localityName = this.model.locality.name;
            }
            /*else {
                var locality = {name:localityName}
                this.model.locality = locality;
            }*/
        }

        if (!municipalityName) {
            if (this.model.municipality) {//ho settato la municipality dal campo Indirizzo o Municipalità
                municipalityName = this.model.municipality.name;
            }
            /*else {
                var municipality = {name:municipalityName}
                this.model.municipality = municipality;
            }*/
        }

        if (!provinceName) {
            if (this.model.province) {//ho settato la province dal campo Indirizzo o Provincia
                provinceName = this.model.province.name;
            }
            /*else {
                var province = {name:provinceName}
                this.model.province = province;
            }*/
        }

        //concateno i campi
        var concat = '';
        if (authorityDescr) {
            concat = authorityDescr;
        }
        if (depDescription) {
            if (concat.length == 0) {
                concat = concat + depDescription;
            } else {
                concat = concat + ' - ' + depDescription;
            }
        }
        if (pavillonNumber) {
            if (concat.length == 0) {
                concat = concat + pavillonNumber;
            } else {
                concat = concat + ' - ' + pavillonNumber;
            }
        }
        if (pavillonName) {
            if (concat.length == 0) {
                concat = concat + pavillonName;
            } else {
                concat = concat + '  ' + pavillonName;
            }
        }
        if (streetName) {
            if (concat.length == 0) {
                concat = concat + streetName;
            } else {
                concat = concat + ' : ' + streetName;
            }
        }
        if (civicNumber) {
            if (concat.length == 0) {
                concat = concat + civicNumber;
            } else {
                concat = concat + ' , ' + civicNumber;
            }
        }
        if (localityName) {
            if (concat.length == 0) {
                concat = concat + localityName;
            } else {
                concat = concat + '  ' + localityName;
            }
        }
        if (municipalityName) {
            if (concat.length == 0) {
                concat = concat + municipalityName;
            } else {
                concat = concat + ' - ' + municipalityName;
            }
        }
        if (provinceName) {
            concat = concat + ' ( ' + provinceName + ' ) ';
        }
        return concat;
    }


    protected setOtherValue(auth: AuthorityDTO) {
        //let phoneSetted = false;
        if (auth) {
            this.authorityType = {
                id: auth.type,
                description: auth.type
            };
            this.streetName = this.coalesceValue(auth.address.street);
            //this.cc = auth.costCenter;
            //this.model.costCenter = auth.costCenter;
            this.convention = auth.convention;

            if (auth.phoneNumbers && auth.phoneNumbers.length > 0) {
                let actual = this.model.phoneNumber;
                if (!actual) {
                    this.model.phoneNumber = auth.phoneNumbers[0];
                    //phoneSetted = true;
                } else {
                    auth.phoneNumbers.forEach(t => {
                        if (t.indexOf(actual) >= 0) {
                            this.model.phoneNumber = t;
                            //phoneSetted = true;
                        }
                    });
                }
            }
            this.phoneNumberSelected = this.model.phoneNumber
            //else{
            //    this.model.phoneNumber= undefined;
            //}            
        } else {
            //this.authorityType = null;
            //this.convention = null;
            this.model.authority = null;
            if (this.model.street)
                this.streetName = this.model.street.name;
        }
        //if(!phoneSetted)
        //  this.model.phoneNumber=null;
    }

    protected getCompact(auth: AuthorityDTO): string {
        return auth.description + ':'
            + this.coalesceValue(auth.address.street) + ' '
            + this.coalesce(auth.address.civicNumber) + ' '
            + this.coalesceValue(auth.address.locality) + ' '
            + this.coalesceValue(auth.address.municipality) + ' '
            + this.coalesceValue(auth.address.province);
    }

    public setDepartmentAndAuthority(result: DepartmentInfoDTO) {
        this.componentService.getRootComponent().mask();
        let body = { id: result.id };
        this.authorityService.searchAuthorityByDepId(body).subscribe(auth => {
            this.componentService.getRootComponent().mask();
            
            let phoneNumberSearch = this.model.phoneNumber;
            this.detail = (result.pavilionName ? result.pavilionName : '') + (result.pavilionNumber ? ' ' + result.pavilionNumber : '');
            
            //il servizio searchAuthorityByDepId carica come unico dipartimento quello in input
            //let newDep = Array<any>();
            //newDep[0] = result;
            //auth.departments = newDep;

            this.setAuthority(auth);
            //setAuthority reset detail and momdel.department

            this.detail = (result.pavilionName ? result.pavilionName : '') + (result.pavilionNumber ? ' ' + result.pavilionNumber : '');
            this.setDepartmentModel(result);

            //this.cc = result.costCenter;
            if (result.costCenter)
                this.model.costCenter = result.costCenter;

            this.authorityType = {
                description: auth.type
            };
            this.convention = auth.convention;
            if (result.phoneNumbers && result.phoneNumbers.length > 0) {
                //this.telephoneList= result.phoneNumbers;
                this.model.telephones = result.phoneNumbers;
                let tel = result.phoneNumbers[0];
                let telSearch = result.phoneNumbers.filter(x => x.indexOf(phoneNumberSearch) > -1);
                if (telSearch.length > 0) tel = telSearch[0];
                this.model.phoneNumber = tel;
            } else if (auth.phoneNumbers && auth.phoneNumbers.length > 0) {
                this.model.phoneNumber = auth.phoneNumbers[0];
            }
            this.phoneNumberSelected = this.model.phoneNumber;
            this.componentService.getRootComponent().unmask();
        });
    }

    public setDepartmentModel(dep: DepartmentInfoDTO) {
        //this.model.authority = dep.authority;
        /*this.model.*/
        this.model.department = dep;
        if (dep) {
            this.model.department.costCenter = dep.costCenter;
            this.model.department.id = dep.id;
            this.model.department.description = dep.description;
            this.model.department.compact = dep.compact;
            this.model.department.pavilionName = dep.pavilionName;
            this.model.department.pavilionNumber = dep.pavilionNumber;
            this.model.department.phoneNumbers = dep.phoneNumbers;
            /*this.model.department = {
                id: dep.id,
                name: dep.compact,
                description: dep.description,
                compactDescription: dep.compact

            }*/
            //let body = { id: dep.authority.id };
            //this.componentService.getRootComponent().mask();
        }
        this.componentService.getRootComponent().unmask();
    }

    protected setStreetfromValue() {
        if (this.model.street)
            this.model.street.name = this.streetName;
        else
            this.model.street = { name: this.streetName };
    }


    protected coalesce(val?: string) {
        return val ? val : ''
    }

    protected coalesceValue(val: Value): string {
        if (val && val.name) {
            return val.name
        } else {
            return '';
        }
    }

    updateDepartmentList(name?: string) {
        // TODO aggiungere ricerca reparto

        let filter: DepartmentFilter = {
            descrizione: name ? name.toUpperCase() : undefined,
            nomeEnte: this.model.authority ? this.model.authority.description : undefined,
            tipoEnte: this.authorityType ? this.authorityType.description : undefined,
            convenzione: this.convention ? this.convention : undefined
        };

        this.coreService.departmentByFilter(filter).subscribe(list => {
            this.departmentList.next(list);
            this.componentService.getRootComponent().unmask();
        });        
    }





    updateAuthorityList(name?: string) {

        //console.log('updateAutorityList ');
        //let actualValue = this.authorityList;
        let filter: AuthorityFilterDTO = {};

        if (name) {
            filter.description = name.toUpperCase();
        }

        if (this.authorityType) {
            filter.type = this.authorityType.description;
        }

        if (this.convention) {
            filter.convention = this.convention;
        }

        filter.withPhone = true;
        //this.componentService.getRootComponent().mask();
        this.authorityService.searchAuthorityByFilter(filter).catch((err, c) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(list => {
            //console.log(list);
            if (list && list.length > 0) {
                this.departmentList = new Subject();
            }
            this.authorityList.next(list);
            this.componentService.getRootComponent().unmask();
        });
    }

    filterMunicipality: { name: string, provinceName: string };

    updateMunicipalityList($event) {

        let name: string = $event;
        // let filter ;

        let provincia: string = undefined;

        if (this.model.province) {
            provincia = this.model.province.name;
        }
        this.filterMunicipality = {
            name: null,
            provinceName: null
        };
        /*

        POST /api/secure/rest/streetmap/searchMunicipality
        FILTRO : {  "exactName": true,  "fetchRule": "string",  "name": "string",  "provinceName": "string" }
        */
        if (provincia)
            this.filterMunicipality = {
                name: name ? name.toUpperCase() : '',
                provinceName: provincia ? provincia.toUpperCase() : ''
            };
        else
            this.filterMunicipality = { name: name ? name.toUpperCase() : '', provinceName: null };

        this.streetService.searchMunicipality(this.filterMunicipality).catch((err, c) => {
            //this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(list => {
            if (list.length == 0) {


                let newMunicipality: Value = {};
                newMunicipality.compactDescription = this.filterMunicipality.name;
                newMunicipality.description = this.filterMunicipality.name;
                newMunicipality.name = this.filterMunicipality.name;
                list.push(newMunicipality);
                this.model.municipality = newMunicipality;

            }
            list = _.sortBy(list, ['name', 'description']);
            this.municipalityList.next(list);
            this.componentService.getRootComponent().unmask();
        });
        //this.componentService.getRootComponent().unmask();
    }

    municipalitySelected($event) {
        if ($event) {
            let comuneSelezionato: Value = $event;
            if (!this.model.municipality || comuneSelezionato.id !== this.model.municipality.id) {
                console.log("Il comune e' cambiato: " + $event);
                this.model.locality = undefined;
                this.localityList.next(undefined);


                let filterLocality = {
                    exactName: false,
                    municipalityId: comuneSelezionato.id,
                    municipalityName: comuneSelezionato.name,
                    provinceName: null
                };

                // if(this.model.municipality && this.model.municipality.name){
                //     filterLocality.provinceName =this.model.municipality.name;                    
                // }
                if (this.model.province && this.model.province.name) {
                    filterLocality.provinceName = this.model.province.name;
                }

                this.streetService.searchLocality(filterLocality).catch((err, c) => {
                    //this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(list => {
                    if (list && list.length > 0) {
                        list = _.sortBy(list, ['name', 'description']);
                        this.localityList.next(list);

                        if (list.length == 1) {
                            this.model.locality = list[0];
                        }
                    } else
                        this.localityList.next(list);
                    // this.componentService.getRootComponent().unmask();
                });



                if (!this.model || !this.model.province || this.model.province.name !== comuneSelezionato.extraInfo.parentId) {
                    let filterProvince: ProvinceFilterByMunicipalityDTO = {
                        municipalityId: comuneSelezionato ? comuneSelezionato.id : '',
                        municipalityName: comuneSelezionato ? comuneSelezionato.name.toUpperCase() : ''
                    };

                    this.streetService.searchProvinceByMunicipalityName(filterProvince).catch((err, c) => {
                        //this.componentService.getRootComponent().unmask();
                        return [];
                    }).subscribe(list => {
                        if (list && list.length > 0) {
                            list = _.sortBy(list, ['name', 'description']);
                            this.provinceList.next(list);

                            if (list.length == 1) {
                                this.model.province = list[0];
                            }

                        } else
                            this.provinceList.next(list);
                        // this.componentService.getRootComponent().unmask();
                    });
                }
            }
        }
    }


    setTelephoneNumber(number) {
        this.model.phoneNumber = number.text;
    }


    updateLocalityList(name?: string) {
        // let filter = { name: name ? name.toUpperCase() : '' };
        this.filterLocality = {
            name: (name ? name.toUpperCase() : ''),
            municipalityName: (this.model.municipality ? this.model.municipality.name : null),
            municipalityId: (this.model.municipality ? this.model.municipality.id : null),
            provinceName: (this.model.province ? this.model.province.name : null),
        };

        this.streetService.searchLocality(this.filterLocality).catch((err, c) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(list => {
            if (list && list.length > 0) {
                list = _.sortBy(list, ['name', 'description']);
                this.localityList.next(list);
            } else {
                let newLocality: Value = {};
                newLocality.compactDescription = this.filterLocality.name;
                newLocality.description = this.filterLocality.name;
                newLocality.name = this.filterLocality.name;
                list.push(newLocality);
                this.model.locality = newLocality;
                this.localityList.next(list);
            }
            this.componentService.getRootComponent().unmask();
        });

    }

    localitySelected($event) {
        if ($event) {
            let localitaSelezionata: Value = $event;
            //if(!this.model.municipality || localitaSelezionata.id!==this.model.municipality.id){
            console.log("La localita' e' cambiata: " + $event);
            // this.model.locality=undefined;
            // this.localityList.next(undefined);   


            let reloadMunicipalityList: boolean = true;
            let reloadProvinceList: boolean = true;
            let filterLocality = { exactName: false, municipalityName: undefined, name: undefined, provinceName: undefined };

            if (localitaSelezionata && localitaSelezionata.extraInfo && this.model) {

                if (this.model.municipality && localitaSelezionata.extraInfo.municipalityName == this.model.municipality.name) {
                    reloadMunicipalityList = false;
                } else {
                    filterLocality.exactName = true;
                    filterLocality.provinceName = localitaSelezionata.extraInfo.provinceName;
                    filterLocality.name = localitaSelezionata.extraInfo.municipalityName;
                }

                if (this.model.province && localitaSelezionata.extraInfo.provinceName === this.model.province.name) {
                    reloadProvinceList = false;
                } else {
                    filterLocality.exactName = true;
                    filterLocality.provinceName = localitaSelezionata.extraInfo.provinceName;
                    filterLocality.name = localitaSelezionata.extraInfo.municipalityName;
                }
            }
            console.log("Nome Localita'     : " + localitaSelezionata.name);
            console.log("Nome municipalita' : " + localitaSelezionata.extraInfo.municipalityName);
            console.log("Nome provincia     : " + localitaSelezionata.extraInfo.provinceName);
            console.log("Ricaricare la lista dei comuni     : " + reloadMunicipalityList);
            console.log("Ricaricare la lista delle province : " + reloadProvinceList);
            //Carica la provincia associata 
            if (reloadProvinceList) {
                this.componentService.getRootComponent().mask();
                this.streetService.searchProvince(localitaSelezionata.extraInfo.provinceName).catch((err, c) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(list => {
                    if (list && list.length > 0) {
                        this.provinceList.next(list);
                        this.model.province = list[0];
                        //Carica il comune associato
                        let filterComuni = {
                            name: localitaSelezionata.extraInfo.municipalityName ? localitaSelezionata.extraInfo.municipalityName.toUpperCase() : '',
                            exactName: localitaSelezionata.extraInfo.municipalityName ? true : false, provinceName: this.model.province.name ? this.model.province.name.toUpperCase() : ''
                        };

                        this.streetService.searchMunicipality(filterComuni).catch((err, c) => {
                            this.componentService.getRootComponent().unmask();
                            return [];
                        }).subscribe(list => {
                            if (list && list.length > 0) {
                                list = _.sortBy(list, ['name', 'description']);
                                this.municipalityList.next(list);
                                this.model.municipality = list[0];
                            } else
                                this.municipalityList.next(list);

                            this.componentService.getRootComponent().unmask();
                        });

                    } else {
                        this.provinceList.next(undefined);
                    }
                });
            } else if (reloadMunicipalityList) {

                this.componentService.getRootComponent().mask();

                this.streetService.searchMunicipality(filterLocality).catch((err, c) => {
                    this.componentService.getRootComponent().unmask();
                    return [];
                }).subscribe(list => {
                    if (list && list.length > 0) {
                        list = _.sortBy(list, ['name', 'description']);
                        this.municipalityList.next(list);
                        this.model.municipality = list[0];
                    } else
                        this.municipalityList.next(list);

                    this.componentService.getRootComponent().unmask();
                });
            }




        }
    }


    filterProvince: string;


    updateProvinceList(name?: string) {
        name = name ? name.toUpperCase() : '';

        if (name === '') {
            name = '%';
        }

        this.filterProvince = name;
        this.streetService.searchProvince(this.filterProvince).catch((err, c) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(list => {
            if (list && list.length > 0) {
                list = _.sortBy(list, ['name', 'description']);
                this.provinceList.next(list);
            } else {
                let newProvince: Value = {};
                newProvince.compactDescription = this.filterProvince;
                newProvince.description = this.filterProvince;
                newProvince.name = this.filterProvince;
                list.push(newProvince);
                this.model.province = newProvince;
                this.provinceList.next(list);
            }
            //this.provinceList.next(list);                      
        });
    }


    provinceSelected($event) {
        if ($event) {
            let provinciaSelezionata: Value = $event;

            if (!this.model.province || provinciaSelezionata.id !== this.model.province.id) {
                console.log("La provincia è cambiata: " + $event);

                let provinceName: string = $event.name;
                if (this.model && this.model.municipality && this.model.municipality.extraInfo && this.model.municipality.extraInfo.parentId) {
                    let provinciaComuneSelezionato: string = this.model.municipality.extraInfo.parentId;

                    if (provinciaComuneSelezionato !== provinceName) {
                        this.model.municipality = undefined;
                        this.model.locality = undefined;
                        this.localityList.next(undefined);
                        this.municipalityList.next(undefined);
                    }
                }
            }
        }
    }






    searchStreet() {
        if (!this.streetName || this.streetName.length < 2) {
            this.componentService.getRootComponent().showModal('Ricerca indirizzo', 'Inserisci almeno due cifre');
            return;
        }
        let req: StreetFilterDTO = {
            name: this.streetName ? this.streetName.toUpperCase() : '',
            localityName: this.model.locality ? this.model.locality.name : '',
            municipalityName: this.model.municipality ? this.model.municipality.name : '',
            provinceName: this.model.province ? this.model.province.name : ''
        }

        this.componentService.getRootComponent().mask();

        this.streetService.searchStreet(req).catch((err, c) => {
            this.componentService.getRootComponent().unmask();
            return [];
        }).subscribe(value => {
            //console.log(value);
            let modal = this.modalService.open(ResultGridModalComponent, { size: 'lg' });

            modal.componentInstance.list = value;
            modal.componentInstance.title = 'Strade';
            modal.componentInstance.columns = [
                { title: 'Nome', name: 'name' },
                { title: 'Località', name: 'localityName' },
                { title: 'Comune', name: 'municipalityName' },
                { title: 'Provincia', name: 'provinceName' }
            ];
            modal.result.then((result) => {
                if (result) {
                    this.model.locality = { name: result.localityName };
                    this.model.municipality = { name: result.municipalityName };
                    this.model.province = { name: result.provinceName };
                    this.model.street = { name: result.name };
                    this.streetName = result.name;
                    this.model.compactAddress = result.name;
                }
            }, (reason) => {
            });
            this.componentService.getRootComponent().unmask();
        });
    }

    public searchDepartmentByCC(cc: string) {
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


    public searchDepartmentByPhone(phone: string) {

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



    protected setDepartments(value: Array<DepartmentInfoDTO>) {
        this.componentService.getRootComponent().mask();
        if (value && value.length == 1) {
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
        this.componentService.getRootComponent().unmask();
        modal.result.then((result) => {

            if (result) {
                this.setDepartmentAndAuthority(result);
            }
        }, (reason) => {
            console.log("modal result error ");
        });


    }


    protected searchAuthority(cc: string, telephone: string) {

        if (cc) {

            this.componentService.getRootComponent().mask();
            let body = { costCenter: cc };
            this.authorityService.searchAuthorityByCostCenter(body).catch((e, o) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(value => {
                this.setAuthorities(value);
                this.componentService.getRootComponent().unmask();
            });
        }

        if (telephone) {
            this.componentService.getRootComponent().mask();
            let body = { telephone: telephone };
            this.authorityService.searchAuthorityByTelephone(body).catch((e, o) => {
                this.componentService.getRootComponent().unmask();
                return [];
            }).subscribe(value => {
                this.setAuthorities(value);
                this.componentService.getRootComponent().unmask();
            });
        }
    }


    public localityMinChar(): number {
        if (this.model && (this.model.municipality || this.model.province)) {
            return 0;
        }
        return 2;
    }

    public municipalityMinChar(): number {
        if (this.model && this.model.province) {
            return 0;
        }
        return 2;
    }
}

interface AuthorityInfoDTOExt extends AuthorityInfoDTO {
    telephoneList?: string;
    telephones?;
}
