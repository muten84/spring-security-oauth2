export * from './time.service';
import { TimeService } from './time.service';
export * from './localbus.service';
import { LocalBusService } from './localbus.service';
export * from './websocket.service';
import { WebSocketService } from './websocket.service';
import { DeviceOrientationService } from './device-orientation'
export * from './device-orientation';
import { StorageService } from './storage.service'
export * from './storage.service';
export const CORE_APIS = [TimeService, LocalBusService, WebSocketService, DeviceOrientationService, StorageService];
