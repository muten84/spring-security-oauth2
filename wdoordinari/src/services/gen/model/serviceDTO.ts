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
import { InterventionDTO } from './interventionDTO';
import { TsServiceResourceDTO } from './tsServiceResourceDTO';


export interface ServiceDTO { 
    id?: string;
    code?: string;
    creationDate?: Date;
    creationUser?: string;
    managingDate?: Date;
    managingUser?: string;
    transportDate?: Date;
    closureDate?: Date;
    closureUser?: string;
    startAddress?: string;
    endAddress?: string;
    pause?: number;
    pauseMu?: string;
    km?: number;
    priority?: string;
    status?: string;
    paid?: boolean;
    note?: string;
    companionType?: string;
    companionName?: string;
    totalKm?: number;
    expectedCharge?: string;
    totalCharge?: string;
    serviceName?: string;
    interventionList?: Array<InterventionDTO>;
    serviceResourceList?: Array<TsServiceResourceDTO>;
    source?: string;
}