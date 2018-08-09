import { InterventionActive } from '../../../tablet-layout/intervention-layout/intervention.active.interface';
import { Inject, Injectable, Optional } from '@angular/core';
import { PatientItem } from './PatientItem.interface';

export interface PatientStatus {


    listPatient: Array<PatientItem>;

    currentPatient: PatientItem;

    staticDataStatus: boolean;


}