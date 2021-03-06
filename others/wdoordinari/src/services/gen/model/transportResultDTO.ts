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
import { MenuItemValue } from './menuItemValue';


export interface TransportResultDTO { 
    id?: string;
    code?: string;
    creationDate?: Date;
    creationUser?: string;
    managingDate?: Date;
    managingUser?: string;
    transportDate?: Date;
    transportDateValue?: Date;
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
    resources?: string;
    patientsNumber?: number;
    patients?: Array<string>;
    redBall?: boolean;
    alarm?: TransportResultDTO.AlarmEnum;
    popupMenu?: Array<MenuItemValue>;
    rowStyle?: { [key: string]: string; };
    source?: string;
}
export namespace TransportResultDTO {
    export type AlarmEnum = 'ALARM' | 'TOO_LATE' | 'NO_ALARM';
    export const AlarmEnum = {
        ALARM: 'ALARM' as AlarmEnum,
        TOOLATE: 'TOO_LATE' as AlarmEnum,
        NOALARM: 'NO_ALARM' as AlarmEnum
    };
}
