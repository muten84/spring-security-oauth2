export class ChildItem {
    name: string;
    path: string;
}

export class MenuItem {
    sectionId: string;
    name: string;
    path: string;
    icon: string;
    iClass: string;
    childItems: ChildItem[];


    constructor() { }
}