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
import { Department } from './department';
import { Value } from './value';


export interface AddressBookingDTO { 
    municipality: Value;
    locality?: Value;
    province: Value;
    street?: Value;
    civicNumber?: string;
    authority?: AuthorityDTO;
    department?: Department;
    compactAddress?: string;
    phoneNumber?: string;
    costCenter?: string;
    telephones?: Array<string>;
    compoundAddress?: string;
}