/**
 * Swagger Maven Plugin Sample
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import * as models from './models';

export interface TurnDTO {
    id?: string;

    parkingId?: string;

    parkingCode?: string;

    vehicleId?: string;

    vehicleCode?: string;

    startTurnDate?: Date;

    endTurnDate?: Date;

    conventionId?: string;

    conventionDescription?: string;

    sanitaryCategoryId?: string;

    sanitaryCategoryDescription?: string;

    availability?: boolean;

    note?: string;

    startStandByDate?: Date;

    startStandByUserId?: string;

    endStandByDate?: Date;

    endStandByUserId?: string;

    pause?: boolean;

    startPauseDate?: Date;

    startPauseUserId?: string;

    endPauseDate?: Date;

    endPauseUserId?: string;

    vehicleProfileId?: string;

}
