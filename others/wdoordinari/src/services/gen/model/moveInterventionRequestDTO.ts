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


export interface MoveInterventionRequestDTO { 
    serviceCode: string;
    indexFrom: number;
    shift: MoveInterventionRequestDTO.ShiftEnum;
}
export namespace MoveInterventionRequestDTO {
    export type ShiftEnum = 'UP' | 'DOWN' | 'FIRST' | 'LAST' | 'FIRST_FREE';
    export const ShiftEnum = {
        UP: 'UP' as ShiftEnum,
        DOWN: 'DOWN' as ShiftEnum,
        FIRST: 'FIRST' as ShiftEnum,
        LAST: 'LAST' as ShiftEnum,
        FIRSTFREE: 'FIRST_FREE' as ShiftEnum
    };
}
