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
import { ServiceDTO } from './serviceDTO';
import { TsServiceResourceDTO } from './tsServiceResourceDTO';


export interface RequestInterventionUnifiedService { 
    trasportoCorrente: ServiceDTO;
    prenotazioneCorrente: InterventionDTO;
    risorsaDelServizio: TsServiceResourceDTO;
    km?: number;
    modificaTappePrecedenti?: boolean;
    pause?: number;
    pauseType?: string;
    orarioTappa?: Date;
}