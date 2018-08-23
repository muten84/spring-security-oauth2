import { GpsStatus } from './GpsStatus';
import { GpsCoord } from './GpsCoord';
import { ConnectionStatus } from './ConnectionStatus';
import { BatteryStatus } from './BatteryStatus';

export interface DeviceStatus {
    gpsStatus?: GpsStatus;

    connectionStatus?: ConnectionStatus;

    batteryStatus?: BatteryStatus;

    gpsCoord?: GpsCoord;
}