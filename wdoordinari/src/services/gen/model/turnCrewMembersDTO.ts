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
import { CrewMemberDTO } from './crewMemberDTO';
import { TurnDTO } from './turnDTO';


export interface TurnCrewMembersDTO { 
    id?: string;
    crewMember?: CrewMemberDTO;
    name?: string;
    note?: string;
    turn?: TurnDTO;
    surname?: string;
    task?: string;
    taskDetail?: string;
    startTurnCrew?: Date;
    endTurnCrew?: Date;
}