/**
 * Swagger Maven Plugin Sample
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: v2
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


export interface AdvancedBookingSearchFilterDTO { 
    transportDateStart?: Date;
    transportDateStop?: Date;
    codeList?: Array<string>;
    codeWebList?: Array<string>;
    typeTxtList?: Array<string>;
    statusList?: Array<string>;
    excludeStatus?: boolean;
    withAttach?: boolean;
    returnReady?: boolean;
    assignedParkingCodeList?: Array<string>;
    optionedParkingCodeList?: Array<string>;
    conventionStartList?: Array<string>;
    authorityIdStartList?: Array<string>;
    depIdStartList?: Array<string>;
    conventionEndList?: Array<string>;
    authorityIdEndList?: Array<string>;
    depIdEndList?: Array<string>;
    municipalityStartNameList?: Array<string>;
    localityStartNameTxtList?: Array<string>;
    provinceStartNameList?: Array<string>;
    streetNameStartTxtList?: Array<string>;
    houseNumberStartTxtList?: Array<string>;
    municipalityEndNameList?: Array<string>;
    localityEndNameTxtList?: Array<string>;
    provinceEndNameList?: Array<string>;
    streetNameEndTxtList?: Array<string>;
    houseNumberEndTxtList?: Array<string>;
    patientSurnameTxtList?: Array<string>;
    patientNameTxtList?: Array<string>;
}
