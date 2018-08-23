
export interface SelectItem {
    id?: string;
    text?: string;
    children?: Array<SelectItem>;
    parent?: SelectItem;

}


export function create( source: any, idField: string, textField: string ): SelectItem {
    let value: SelectItem;
 
    if ( typeof source === 'string' ) {
        value = {id: "", text: ""};
        value.id = value.text = source;
    }
    if ( typeof source === 'object' ) {
        value = source;
        value.id = source[idField] || source[textField];
        value.text = source[textField];

        if ( source.children && source.text ) {
            value.children = source.children.map(( c: any ) => {
                let r: SelectItem = create( c, idField, textField );
                r.parent = value;
                return r;
            } );
        }
    }
    return value;
}



export function fillChildrenHash( a: SelectItem, optionsMap: Map<string, number>, startIndex: number ): number {
    let i = startIndex;
    a.children.map(( child: SelectItem ) => {
        optionsMap.set( child.id, i++ );
    } );
    return i;
}

export function hasChildren( a: SelectItem ): boolean {
    return a.children && a.children.length > 0;
}

export function getSimilar( a: SelectItem ): SelectItem {
    return { ...a };
}

