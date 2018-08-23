export interface DumpEvent {
    address?: String,
    alarmed?: boolean,
    bell?: string,
    bluEvent?: boolean,
    codex?: string,
    criticity?: string,
    dataBackupFlag?: string,
    district?: string,
    emergencyId?: string,
    eventDescriptionTooltip?: string
    hasActivatedAuthorities?: boolean,
    inManagingTooltip?: string,
    inModifyingTooltip?: string,
    latitude?: number,
    localityDistrict?: string,
    localityDistrictgm?: string,
    localityMunicipality?: string
    longitude?: number
    municipalityDistrictgm?: string,
    opCentral?: string
    opCentralTooltip?: string,
    province?: string,
    redEcho?: boolean,
    statusMValue?: string,
    statusSValue?: string
    statusTooltip?: string,
    status_G?: string,
    status_M?: string,
    status_S?: string,
    textBgroundCriticity?: string,
    textColorCriticity?: string,
    tiu?: boolean,
    tiuAddressTooltip?: string,
    tiuLocalityMunicipalityTooltip?: string,
    valid?: string,
    manageVehicleForSynoptics: Array<DumpIntervention>

}

export interface DumpIntervention {
    assignedVehicleType?: string,
    criticalPatient?: string,
    dateActivation?: string,
    eventType?: string,
    hasPassenger: boolean,
    hospital?: string,
    interventionEvolutionStatus?: string
    mobileCode?: string,
    moreDestination?: boolean,
    municipalityName?: string
    numberPositionVehicle?: string,
    vehicle?: string,
    vehicleCode?: string
    vehicleId?: string,
    vehicleProfile?: string
}