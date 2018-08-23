export interface CurrentIntervention {

    checkedStatus: Array<CheckedStatus>;

}

export interface CheckedStatus {
    code?: number;
    sublabel?: string;
}