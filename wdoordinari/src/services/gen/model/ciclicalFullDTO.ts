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
import { CiclicalBookingDTO } from './ciclicalBookingDTO';
import { CiclicalDayDTO } from './ciclicalDayDTO';
import { CiclicalDaysInfoDTO } from './ciclicalDaysInfoDTO';
import { CiclicalHistoryDTO } from './ciclicalHistoryDTO';


export interface CiclicalFullDTO { 
    ciclicalBooking?: CiclicalBookingDTO;
    id?: string;
    bookingId?: string;
    bookingCode?: string;
    fromDate?: Date;
    status?: string;
    toDate?: Date;
    parking?: string;
    transportDate?: string;
    modificationDate?: Date;
    modificationUser?: string;
    statusHistory?: string;
    createdBy?: string;
    ciclicalDays?: Array<CiclicalDayDTO>;
    ciclicalDaysInfo?: Array<CiclicalDaysInfoDTO>;
    minCiclicalDayInfo?: CiclicalDaysInfoDTO;
    maxCiclicalDayInfo?: CiclicalDaysInfoDTO;
    history?: Array<CiclicalHistoryDTO>;
}
