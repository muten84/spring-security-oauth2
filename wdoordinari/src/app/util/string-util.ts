import * as moment from 'moment';

export function isEmpty(s: string | number) {
    if (!s) {
        return true;
    }
    return s.toString().length <= 0;
}


export function randomString(length?: number): string {
    if (!length) {
        length = 5;
    }
    let text = "";
    let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (let i = 0; i < length; i++)
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}