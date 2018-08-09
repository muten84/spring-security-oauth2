
import { InjectionToken } from '@angular/core';

export const BASE_PATH = new InjectionToken('basePath');
export class Configuration {
    apiKey: string;
    username: string;
    password: string;
    accessToken: string | (() => string);
    withCredentials: boolean;
}

const RPC_MAP = {
    "SBS": ""
}
export function rpcOperationToOp() {

}