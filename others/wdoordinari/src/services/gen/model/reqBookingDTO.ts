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
import { Value } from './value';


export interface ReqBookingDTO { 
    authority?: Value;
    department?: Value;
    reference?: string;
    description?: string;
    startTelephone?: string;
    telephone?: string;
    type?: string;
    convention: string;
    detail?: string;
    vat?: string;
    cc?: string;
}
