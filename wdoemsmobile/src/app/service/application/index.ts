export * from './application-status.service';
import { ApplicationStatusService } from './application-status.service';
export * from './device-status.service';
import { DeviceStatusService } from './device-status.service';
export * from './resource-list.service';
import { ResourceListService } from './resource-list.service';
export * from './patient-status.service';
import { PatientStatusService } from './patient-status.service';

export const APPLICATION_API = [ApplicationStatusService, DeviceStatusService, ResourceListService];
