import { PatientItem } from "../model/index";

export interface PatientCommand {
    evcd: string;
    phase?: number;
    patient: PatientItem
}