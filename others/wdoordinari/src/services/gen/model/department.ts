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
import { AuthorityDTO } from './authorityDTO';
import { Value } from './value';


export interface Department { 
    id?: string;
    authority?: Value;
    authorityDTO?: AuthorityDTO;
    compact?: string;
    serviceType?: string;
    description?: string;
    streetName?: string;
    houseNumber?: string;
    locality?: string;
    municipality?: string;
    province?: string;
    costCenter?: string;
    reference?: string;
    note?: string;
    pavilionNumber?: string;
    pavilionName?: string;
    phoneNumbers?: Array<string>;
}