import { ItemDetail } from './item-detail.interface';

export function createInterventionObject(): InterventionActive {
  let i: InterventionActive = {
    luogo: createInterventionField(),
    patologia: createInterventionField(),
    civico: createInterventionField(),
    codEmergenza: createInterventionField(),
    comune: createInterventionField(),
    criticita: createInterventionField(),
    dataOraSegnalazione: createInterventionField(),
    eta: createInterventionField(),
    indirizzo: createInterventionField(),
    localita: createInterventionField(),
    luogoPubblico: createInterventionField(),
    modAttivazione: createInterventionField(),
    note: createInterventionField(),
    nPazienti: createInterventionField(),
    personaRif: createInterventionField(),
    piano: createInterventionField(),
    sex: createInterventionField(),
    sirena: createInterventionField(),
    telefono: createInterventionField(),
    position: undefined, 
    patientNnReperito:  {
      label: "",
      value: false
    }
  }
  return i;
}

export function createInterventionField(label?, value?): InterventionField {
  if (!label || !value) {
    let f: InterventionField = {
      label: "",
      value: ""
    }

    return f;
  }
  else {
    let f: InterventionField = {
      label: label,
      value: value
    }

    return f;
  }
}

export interface InterventionField {
  label: string,
  value: string,
  extra?: {}
}

export interface InterventionActive {
  luogo?: InterventionField;
  patologia?: InterventionField;
  criticita?: InterventionField;
  criticitaObject?: ItemDetail;
  modAttivazione?: InterventionField;
  sirena?: InterventionField;
  comune?: InterventionField;
  localita?: InterventionField;
  localitaObject?: ItemDetail;
  indirizzo?: InterventionField;
  indirizzoObject?: ItemDetail;
  luogoPubblico?: InterventionField;
  luogoPubblicoObject?: ItemDetail;
  civico?: InterventionField;
  piano?: InterventionField;
  codEmergenza?: InterventionField;
  codEmergenzaObject?: ItemDetail;
  dataOraSegnalazione?: InterventionField;
  dataOraSegnalazioneObject?: ItemDetail;
  personaRif?: InterventionField;
  personaRifObject?: ItemDetail;
  telefono?: InterventionField;
  nPazienti?: InterventionField;
  sex?: InterventionField;
  eta?: InterventionField;
  note?: InterventionField
  noteAfterBreak?: string;
  noteObject?: ItemDetail;
  interviewList?: Array<any>;
  notifyUpdate?: boolean;
  listLabelUpdate?: string;
  typeIntervention?: string;
  position?: any;
  patientNnReperito?:{
    label: string;
    value: boolean;
  }
};


