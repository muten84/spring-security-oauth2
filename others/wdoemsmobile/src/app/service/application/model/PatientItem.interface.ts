export function createPatientObject(): PatientItem {
  let i: PatientItem = {
    id: undefined,
    idArray: undefined,
    valutazSanitaria: undefined,
    destinazione: createPatientField(),
    esito: createPatientField(),
    accompDa: createPatientField(),
    nome: undefined,
    cognome: undefined,
    checkSconosc: false,
    sex: undefined,
    dataNascita: undefined,
    eta: undefined,
    typeEta: createPatientField(),
    cittadinanza: createPatientField(),
    comune: createPatientField(),
    classePatologia: createPatientField(),
    patologia: createPatientField(),
    prestazPrinc: createPatientField(),
    prestazSec1: createPatientField(),
    prestazSec2: createPatientField(),
    prestazSec3: createPatientField(),
    prestazSec4: createPatientField(),
    deleted: false,
    synchronized: true,
    sent: false,
    dirty: false,
    criticitaFine: undefined,
    owner: undefined,
    coId: undefined,
    otherValuations: [],
    ownerVehicle: undefined,
    pcards: []
  }
  return i;
}

/*i patienti field vengono creati non undefined*/
export function creatEmptyPatientObject(): PatientItem {
  let i: PatientItem = {
    id: undefined,
    idArray: undefined,
    valutazSanitaria: undefined,
    destinazione: {
      id: undefined,
      description: undefined
    },
    esito: {
      id: undefined,
      description: undefined
    },
    accompDa: {
      id: undefined,
      description: undefined
    },
    nome: undefined,
    cognome: undefined,
    checkSconosc: false,
    sex: undefined,
    dataNascita: undefined,
    eta: undefined,
    typeEta: {
      id: undefined,
      description: undefined
    },
    cittadinanza: {
      id: undefined,
      description: undefined
    },
    comune: {
      id: undefined,
      description: undefined
    },
    classePatologia: {
      id: undefined,
      description: undefined
    },
    patologia: {
      id: undefined,
      description: undefined
    },
    prestazPrinc: {
      id: undefined,
      description: undefined
    },
    prestazSec1: {
      id: undefined,
      description: undefined
    },
    prestazSec2: {
      id: undefined,
      description: undefined
    },
    prestazSec3: {
      id: undefined,
      description: undefined
    },
    prestazSec4: {
      id: undefined,
      description: undefined
    },
    deleted: false,
    synchronized: true,
    sent: false,
    dirty: true,
    criticitaFine: undefined,
    owner: undefined,
    coId: undefined,
    otherValuations: [],
    ownerVehicle: undefined,
    pcards: []
  }
  return i;
}

export function createPatientField(id?, description?): PatientField {
  if (!id && !description) {
    return undefined;
  }
  else {
    let f: PatientField = {
      id: id,
      description: description
    }

    return f;
  }
}

export interface PatientField {
  id: string,
  description: string,
  disabled?: boolean,
  refCode?: string
}

export interface PatientItem {
  id?: number;
  idArray?: number;
  valutazSanitaria?: string;
  destinazione?: PatientField;
  esito?: PatientField;
  accompDa?: PatientField;
  nome?: string;
  cognome?: string;
  checkSconosc?: boolean;
  sex?: string;
  dataNascita?: Date;
  eta?: number;
  typeEta?: PatientField;
  cittadinanza?: PatientField;
  comune?: PatientField;
  classePatologia?: PatientField;
  patologia?: PatientField;
  prestazPrinc?: PatientField;
  prestazSec1?: PatientField;
  prestazSec2?: PatientField;
  prestazSec3?: PatientField;
  prestazSec4?: PatientField;
  owner: string;
  otherValuations: Array<any>;
  deleted?: boolean;
  dirty?: boolean;
  criticitaFine?: string;
  criticitaFineId?: string;
  synchronized?: boolean;
  sent: boolean;
  coId?: string;
  ownerVehicle: string;
  pcards: Array<any>;
};





