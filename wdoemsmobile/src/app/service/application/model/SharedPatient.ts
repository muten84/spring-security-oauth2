import { PatientField } from './PatientItem.interface';

export interface SharedPatient {
    coid: string;
    evcd: string;
    destinazione: PatientField;
    nome: string;
    cognome: string;
    dataNascita: Date;
    eta: number;
    typeEta: PatientField;
    prestazPrinc: PatientField;
    prestazSec1: PatientField;
    prestazSec2: PatientField;
    prestazSec3: PatientField;
    prestazSec4: PatientField;
    comune: PatientField;
    classePatologia: PatientField;
    patologia: PatientField;
    cittadinanza: PatientField;
    nation: PatientField;
    province: PatientField;
    id: number;
    region: PatientField;
    esito: PatientField;
    sex: string;
    valResult: string;
    sanEval: string;
    criticitaFine: string;
    criticitaFineId: string;
    pcards: any;
    owner: string;
    deleted: boolean;
    accompDa: PatientField;
    ownerVehicle: PatientField;

}