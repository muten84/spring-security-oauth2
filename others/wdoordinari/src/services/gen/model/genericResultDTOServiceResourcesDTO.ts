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
import { MessageDTO } from './messageDTO';
import { ServiceResourcesDTO } from './serviceResourcesDTO';


export interface GenericResultDTOServiceResourcesDTO { 
    result?: ServiceResourcesDTO;
    resultList?: Array<ServiceResourcesDTO>;
    msgList?: Array<MessageDTO>;
}
