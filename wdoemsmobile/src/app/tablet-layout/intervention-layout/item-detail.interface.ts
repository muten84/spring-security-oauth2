export interface DetailMap {
    [key: string]: string;


}

export interface TypeDetail {
    label: string;
    value: string;
}

export class ItemDetail {
    detailMap: DetailMap = {};
    detailArray: Array<TypeDetail> = [];
    constructor() {

    }

    getDetail(label: string): string {
        return this.detailMap[label];
    }

    addDetail(label, value: string) {
        this.detailMap[label] = value;
        this.detailArray[this.detailArray.length] = { label: label, value: value };
    }

}