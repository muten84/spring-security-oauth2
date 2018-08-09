import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewEncapsulation, forwardRef, TemplateRef, ContentChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import * as _ from 'lodash';
import { List } from 'lodash';
import { escapeRegexp } from './common';
import { OptionsBehavior } from './select-interfaces';
import { SelectItem, create, fillChildrenHash, getSimilar, hasChildren } from './select-item';
import { stripTags } from './select-pipes';


@Component({
    selector: 'ng-select',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            /* tslint:disable */
            useExisting: forwardRef(() => SelectComponent),
            /* tslint:enable */
            multi: true
        }
    ],
    templateUrl: './select.html',
    styleUrls: ['./select.css'],
    encapsulation: ViewEncapsulation.None
})
export class SelectComponent implements OnInit, ControlValueAccessor {

    private openList = false;
    public showLoading = false;
    public showEmpty = false;
    private useRefresh = true;

    @Input("tabindex") public tabindexNumber = -1;
    @Input() public allowClear = false;
    @Input() public placeholder = '';
    @Input() public idField = 'id';
    @Input() public textField = 'text';
    @Input() public valueField: string;
    @Input() public refreshDelay = 300;
    @Input() public minChar = 3;
    @Input() public queryLike = true;


    @Input() public childrenField = 'children';
    @Input() public multiple = false;

    @ContentChild(TemplateRef) template: TemplateRef<any>;

    @Input()
    public set items(value: Array<any>) {
        this.showLoading = false;
        if (!value || value.length == 0) {
            if (this.hasFocus()) {
                this.showEmpty = true;
            }
            this._items = this.itemObjects = [];
        } else {
            this.showEmpty = false;
            this._items = value.filter((item: any) => {
                if ((typeof item === 'string') || (typeof item === 'object' && item && item[this.textField] && item[this.idField])) {
                    return item;
                }
            });
            this.itemObjects = this._items.map((item: any) => {

                return create(item, this.idField, this.textField);
            });
        }

        this.updateOptions();

        if (this.openList) {
            if (this.optionsOpened) {
                this.optionsOpened = false;
            }
            this.open();
            this.openList = false;
        }
    }

    @Input()
    public set disabled(value: boolean) {
        this._disabled = value;
        if (this._disabled === true) {
            this.hideOptions();
        }
    }

    public get disabled(): boolean {
        return this._disabled;
    }

    @Input()
    public set active(selectedItems: Array<any>) {
        if (!selectedItems || selectedItems.length === 0) {
            this._active = [];
        } else {

            this._active = selectedItems.map((item: any) => {
                return { ...item };
            });
        }
    }

    @Output() public data: EventEmitter<any> = new EventEmitter();
    @Output() public selected: EventEmitter<any> = new EventEmitter();
    @Output() public removed: EventEmitter<any> = new EventEmitter();
    @Output() public typed: EventEmitter<any> = new EventEmitter();
    @Output() public opened: EventEmitter<any> = new EventEmitter();

    @Output() public refresh: EventEmitter<any> = new EventEmitter();


    public options: Array<SelectItem> = [];
    public itemObjects: Array<SelectItem> = [];
    public activeOption: SelectItem;
    public element: ElementRef;
    public refreshTimeout;

    public get active(): Array<any> {
        return this._active;
    }

    public set optionsOpened(value: boolean) {
        this._optionsOpened = value;
        this.opened.emit(value);

        // console.log("caricamento combo completato");
        this.showLoading = false;

    }

    public get optionsOpened(): boolean {
        return this._optionsOpened;
    }

    protected onChange: any = Function.prototype;
    protected onTouched: any = Function.prototype;

    public inputMode = false;
    private _optionsOpened = false;
    private behavior: OptionsBehavior;
    public inputValue = '';
    private _items: Array<any> = [];
    private _disabled = false;
    private _active: Array<SelectItem> = [];

    public constructor(element: ElementRef, private sanitizer: DomSanitizer) {
        this.element = element;
        this.clickedOutside = this.clickedOutside.bind(this);
    }

    public sanitize(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    //appena viene passato il focus all'elemento padre seleziono l'input 
    // @HostListener('focus')
    protected focusedElement() {
        if (this._disabled === true) {
            return;
        }
        this.inputMode = true;
        this.focusToInput();
    }

    @HostListener('blur', ['$event'])
    public onBlur($event: FocusEvent) {
        setTimeout(() => {
            //se il nuovo elemento col focus non è un elemento interno di questa select chiudo tutto 
            if (!this.isDescendant(this.element.nativeElement, document.activeElement)) {
                this.clickedOutside();
            }
        }, 0);
    }

    @HostListener('keydown', ['$event'])
    public keydown($event: any): void {
        if (!this.inputMode) {
            this.inputEvent($event);
        }
    }

    public isDescendant(parent: Element, child: Element) {
        var node = child.parentNode;
        while (node != null) {
            if (node == parent) {
                return true;
            }
            node = node.parentNode;
        }
        return false;
    }

    public inputEvent(e: any, isUpMode: boolean = false): void {
        let specialKey = false;
        this.showEmpty = false;

        //alt - control - shift
        if (e.keyCode === 18 || e.keyCode === 17 || e.keyCode === 16) {
            specialKey = true;
        }

        // tab
        if (e.keyCode === 9 || e.keyCode === 16) {
            return;
        }
        if (isUpMode && (e.keyCode === 37 || e.keyCode === 39 || e.keyCode === 38 ||
            e.keyCode === 40 || e.keyCode === 13)) {
            e.preventDefault();
            return;
        }
        // backspace
        if (!isUpMode && e.keyCode === 8) {
            specialKey = true;
            let el: any = this.element.nativeElement
                .querySelector('div.ui-select-container > input');
            if (!el || !el.value || el.value.length <= 0) {
                if (this.active.length > 0) {
                    this.remove(this.active[this.active.length - 1]);
                }
                this.optionsOpened = false;
                this.inputValue = '';
                e.preventDefault();
            }
            //return;
        }
        // esc
        if (!isUpMode && e.keyCode === 27) {
            specialKey = true;
            this.hideOptions();
            this.element.nativeElement.children[0].focus();
            e.preventDefault();
            return;
        }
        // del
        if (!isUpMode && e.keyCode === 46) {
            specialKey = true;
            if (this.active.length > 0) {
                this.remove(this.active[this.active.length - 1]);
            }
            e.preventDefault();
            //return;
        }
        // left
        if (!isUpMode && e.keyCode === 37 && this._items.length > 0) {
            specialKey = true;
            this.behavior.first();
            e.preventDefault();
            return;
        }
        // right
        if (!isUpMode && e.keyCode === 39 && this._items.length > 0) {
            specialKey = true;
            this.behavior.last();
            e.preventDefault();
            return;
        }
        // up
        if (e.keyCode === 38) {
            specialKey = true;
            this.behavior.prev();
            e.preventDefault();
            return;
        }
        // down
        if (e.keyCode === 40) {
            specialKey = true;
            if (!this.optionsOpened) {
                this.openAndRefresh();
            } else {
                this.behavior.next();
            }
            e.preventDefault();
            return;
        }
        // enter
        if (!isUpMode && e.keyCode === 13) {
            specialKey = true;
            if (this.optionsOpened) {
                //se la lista è aperta inserisco l'elemento selezionato
                if (this.multiple) {
                    if (this.active.indexOf(this.activeOption) === -1) {
                        this.selectActiveMatch();
                        this.behavior.next();
                    }
                } else {
                    this.selectActiveMatch();
                    if (this.active.indexOf(this.activeOption) === -1) {
                        this.behavior.next();
                    }
                }
            } else {
                //altrimenti apro la lista
                this.openAndRefresh();
            }
            e.stopPropagation();
            e.preventDefault();
            return;
        }
        let target = e.target || e.srcElement;
        if (target && target.value) {
            this.inputValue = target.value;
            this.behavior.filter(new RegExp(escapeRegexp(this.inputValue), 'ig'));
            this.doEvent('typed', this.inputValue);
            if (this.useRefresh) {
                this.doRefresh();
            }
        } else if (!this.inputMode) {
            this.inputMode = true;
            if (!specialKey) {
                this.focusToInput(e.key);
            } else {
                this.focusToInput("");
            }
        } else if (e.keyCode !== 8) {
            this.open();
        }
    }

    protected openAndRefresh() {
        if (!this.optionsOpened) {
            if (this.useRefresh) {
                this.items = [];
                this.doRefresh();
            }
            this.open();
        }
    }

    protected doRefresh() {

        if (this.refreshTimeout) {
            clearTimeout(this.refreshTimeout)
            this.refreshTimeout = null;
        }
        //avvio tutto dopo 1 millesimo di secondo per ovviare al problema del numero di caratteri
        setTimeout(() => {

            //svuoto la lista degli items
            this.items = [];
            this.showEmpty = false;
            if (this.minChar == 0 || (this.inputValue && this.inputValue.length >= this.minChar)) {

                this.refreshTimeout = setTimeout(() => {

                    this.showLoading = true;
                    // console.log("caricamento combo iniziato");
                    this.refresh.emit(this.inputValue);

                    this.refreshTimeout = null;
                    this.openList = true;
                }, this.refreshDelay);
            }
        }, 1);
    }

    public ngOnInit(): any {
        this.behavior = (this.firstItemHasChildren) ?
            new ChildrenBehavior(this, this.queryLike) : new GenericBehavior(this, this.queryLike);
        let count = this.refresh.observers.length;
        /*//console.log("la combo è statica? : " + count);*/
        if (count <= 0) {
            this.useRefresh = false;
        }
    }

    public remove(item: SelectItem): void {
        if (this._disabled === true) {
            return;
        }
        if (this.multiple === true && this.active) {
            let index = this.active.indexOf(item);
            this.active.splice(index, 1);
            this.data.next(this.active);
            this.doEvent('removed', item);
        }
        if (this.multiple === false) {
            this.active = [];
            this.data.next(this.active);
            this.doEvent('removed', item);
        }
        this.updateOptions();
    }

    public doEvent(type: string, value: any): void {
        if ((this as any)[type] && value) {
            (this as any)[type].next(value);
        }

        this.onTouched();


        if (type === 'selected' || type === 'removed') {
            let toRet;
            let inpV = this.inputValue;
            if (this.valueField) {
                toRet = this.active.map(a => {
                    return a[this.valueField];
                });
            } else {
                toRet = this.active;
            }
            if (this.multiple) {
                if (!this.valueField) {
                    toRet = toRet.map(e => {
                        let tmp = { ...e };
                        delete tmp.text;
                        return tmp;
                    });
                }
                inpV = '';
            } else {
                toRet = toRet[0];
                if (toRet) {
                    inpV = toRet[this.textField];
                } else {
                    inpV = '';
                }
                if (!this.valueField && toRet) {
                    toRet = { ...toRet };
                    // delete tmp.text;                    
                }
            }

            this.onChange(toRet);
            this.inputValue = inpV;
        }
    }

    public clickedOutside(): void {
        if (this.active.length == 0) {
            this.inputValue = '';
        }
        this.inputMode = false;
        this.optionsOpened = false;
        this.showLoading = false;
        this.showEmpty = false;
    }

    public get firstItemHasChildren(): boolean {
        return this.itemObjects[0] && hasChildren(this.itemObjects[0]);
    }

    public writeValue(val: any): void {
        this.active = [];
        let inpV = '';
        if (val && this.valueField) {
            inpV = val;
            let ret: any;
            if (this.itemObjects.length > 0) {
                ret = _.find(this.itemObjects,
                    (value: SelectItem, index: number, collection: List<SelectItem>) => {
                        return value[this.valueField] === val;
                    });
            } else {
                ret = create(val, this.idField, this.textField);
            }

            this.active[0] = ret;
        } else {
            if (val) {
                if (this.multiple) {
                    this.active = val.map(v => {
                        return create(v, this.idField, this.textField);
                    });
                } else {
                    inpV = val[this.textField];
                    this.active[0] = create(val, this.idField, this.textField);
                }
            }
        }
        this.data.emit(this.active);
        this.inputValue = inpV;
    }

    public registerOnChange(fn: (_: any) => {}): void { this.onChange = fn; }
    public registerOnTouched(fn: () => {}): void { this.onTouched = fn; }



    protected matchClick(e: any): void {
        if (this._disabled === true) {
            return;
        }
        this.inputMode = !this.inputMode;
        if (this.inputMode === true && ((this.multiple === true && e) || this.multiple === false)) {
            let value = '';
            if (this.active.length > 0) {
                value = this.active[0].text;
            }

            this.focusToInput(value);
            //se è una combo dinamica 
            if (this.refresh.observers.length > 0) {
                //   if (this.active.length > 0) {
                this.doRefresh();
                //     } else if (!this.opened) {
                //        this.items = [];
                //    }
            } else {
                this.open();
            }
        }
    }

    protected mainClick(event: any): void {
        if (this.inputMode === true || this._disabled === true) {
            return;
        }
        if (event.keyCode === 46) {
            event.preventDefault();
            this.inputEvent(event);
            return;
        }
        if (event.keyCode === 8) {
            event.preventDefault();
            this.inputEvent(event, true);
            return;
        }
        if (event.keyCode === 9 || event.keyCode === 13 ||
            event.keyCode === 27 || (event.keyCode >= 37 && event.keyCode <= 40)) {
            event.preventDefault();
            return;
        }
        this.inputMode = true;
        let value = '';
        if (this.active.length > 0) {
            value = this.active[0].text;
        } else {
            value = String
                .fromCharCode(96 <= event.keyCode && event.keyCode <= 105 ? event.keyCode - 48 : event.keyCode)
                .toLowerCase();
        }
        this.focusToInput(value);
        this.open();
        let target = event.target || event.srcElement;
        target.value = value;
        this.inputEvent(event);
    }

    protected selectActive(value: SelectItem): void {
        this.activeOption = value;
    }

    protected isActive(value: SelectItem): boolean {
        return (value && this.activeOption && this.activeOption.id === value.id);
    }

    protected removeClick(value: SelectItem, event: any): void {
        event.stopPropagation();
        this.inputValue = '';
        this.remove(value);
    }

    public focusToInput(value: string = ''): void {
        setTimeout(() => {
            let el = this.element.nativeElement.querySelector('div.ui-select-container > input');
            if (el) {
                el.focus();
                el.value = value;
            }
        }, 0);
    }

    private hasFocus(): boolean {
        let el = this.element.nativeElement.querySelector('div.ui-select-container > input');
        if (el) {
            return document.activeElement == el;
        }
    }

    private updateOptions(): void {
        this.options = this.itemObjects
            .filter((option: SelectItem) => (this.multiple === false ||
                this.multiple === true && !this.active.find((o: SelectItem) => option.id === o.id)));
    }


    public toggle() {
        if (this.optionsOpened) {
            this.close();
        } else {
            this.openAndRefresh();
        }
    }

    public close(): void {
        this.optionsOpened = false;
    }

    private open(): void {
        this.updateOptions();

        if (this.options.length > 0) {
            this.behavior.first();
        }
        this.optionsOpened = true;
    }

    private hideOptions(): void {
        this.inputMode = false;
        this.optionsOpened = false;
    }

    private selectActiveMatch(): void {
        this.selectMatch(this.activeOption);
    }

    private selectMatch(value: SelectItem, e: Event = void 0): void {
        if (e) {
            e.stopPropagation();
            e.preventDefault();
        }
        if (this.options.length <= 0) {
            return;
        }
        if (this.multiple === true) {
            this.active.push(value);
            this.data.next(this.active);
        }
        if (this.multiple === false) {
            this.active[0] = value;
            this.data.next(this.active[0]);
        }
        this.doEvent('selected', value);
        this.hideOptions();
        if (this.multiple === true) {
            this.focusToInput('');
        } else {
            this.focusToInput(stripTags(value.text));
            this.element.nativeElement.querySelector('.ui-select-container').focus();
        }
    }
}

export class Behavior {
    public optionsMap: Map<string, number> = new Map<string, number>();

    public actor: SelectComponent;

    public constructor(actor: SelectComponent) {
        this.actor = actor;
    }

    public fillOptionsMap(): void {
        this.optionsMap.clear();
        let startPos = 0;
        this.actor.itemObjects
            .map((item: SelectItem) => {
                startPos = fillChildrenHash(item, this.optionsMap, startPos);
            });
    }

    public ensureHighlightVisible(optionsMap: Map<string, number> = void 0): void {
        let container = this.actor.element.nativeElement.querySelector('.ui-select-choices');
        if (!container) {
            return;
        }
        let choices = container.querySelectorAll('.ui-select-choices-row');
        if (choices.length < 1) {
            return;
        }
        let activeIndex = this.getActiveIndex(optionsMap);
        if (activeIndex < 0) {
            return;
        }
        let highlighted: any = choices[activeIndex];
        if (!highlighted) {
            return;
        }
        let posY: number = highlighted.offsetTop + highlighted.clientHeight - container.scrollTop;
        let height: number = container.offsetHeight;
        if (posY > height) {
            container.scrollTop += posY - height;
        } else if (posY < highlighted.clientHeight) {
            container.scrollTop -= highlighted.clientHeight - posY;
        }
    }

    private getActiveIndex(optionsMap: Map<string, number> = void 0): number {
        let ai = this.actor.options.indexOf(this.actor.activeOption);
        if (ai < 0 && optionsMap !== void 0) {
            ai = optionsMap.get(this.actor.activeOption.id);
        }
        return ai;
    }
}

export class GenericBehavior extends Behavior implements OptionsBehavior {
    public constructor(actor: SelectComponent, private queryLike: boolean) {
        super(actor);
    }

    public first(): void {
        this.actor.activeOption = this.actor.options[0];
        super.ensureHighlightVisible();
    }

    public last(): void {
        this.actor.activeOption = this.actor.options[this.actor.options.length - 1];
        super.ensureHighlightVisible();
    }

    public prev(): void {
        let index = this.actor.options.indexOf(this.actor.activeOption);
        this.actor.activeOption = this.actor
            .options[index - 1 < 0 ? this.actor.options.length - 1 : index - 1];
        super.ensureHighlightVisible();
    }

    public next(): void {
        let index = this.actor.options.indexOf(this.actor.activeOption);
        this.actor.activeOption = this.actor
            .options[index + 1 > this.actor.options.length - 1 ? 0 : index + 1];
        super.ensureHighlightVisible();
    }

    public filter(query: RegExp): void {
        let options = this.actor.itemObjects
            .filter((option: SelectItem) => {
                let match;
                if (this.queryLike) {
                    match = stripTags(option.text).match(query);
                } else {
                    match = stripTags(option.text.toLowerCase()).startsWith(query.source.toLowerCase());
                }

                return match &&
                    (this.actor.multiple === false ||
                        (this.actor.multiple === true && this.actor.active.map((item: SelectItem) => item.id).indexOf(option.id) < 0));
            });
        this.actor.options = options;
        if (this.actor.options.length > 0) {
            this.actor.activeOption = this.actor.options[0];
            super.ensureHighlightVisible();
        }
    }
}

export class ChildrenBehavior extends Behavior implements OptionsBehavior {

    public constructor(actor: SelectComponent, private queryLike: boolean) {
        super(actor);
    }

    public first(): void {
        this.actor.activeOption = this.actor.options[0].children[0];
        this.fillOptionsMap();
        this.ensureHighlightVisible(this.optionsMap);
    }

    public last(): void {
        this.actor.activeOption =
            this.actor
                .options[this.actor.options.length - 1]
                .children[this.actor.options[this.actor.options.length - 1].children.length - 1];
        this.fillOptionsMap();
        this.ensureHighlightVisible(this.optionsMap);
    }

    public prev(): void {
        let indexParent = this.actor.options
            .findIndex((option: SelectItem) => this.actor.activeOption.parent && this.actor.activeOption.parent.id === option.id);
        let index = this.actor.options[indexParent].children
            .findIndex((option: SelectItem) => this.actor.activeOption && this.actor.activeOption.id === option.id);
        this.actor.activeOption = this.actor.options[indexParent].children[index - 1];
        if (!this.actor.activeOption) {
            if (this.actor.options[indexParent - 1]) {
                this.actor.activeOption = this.actor
                    .options[indexParent - 1]
                    .children[this.actor.options[indexParent - 1].children.length - 1];
            }
        }
        if (!this.actor.activeOption) {
            this.last();
        }
        this.fillOptionsMap();
        this.ensureHighlightVisible(this.optionsMap);
    }

    public next(): void {
        let indexParent = this.actor.options
            .findIndex((option: SelectItem) => this.actor.activeOption.parent && this.actor.activeOption.parent.id === option.id);
        let index = this.actor.options[indexParent].children
            .findIndex((option: SelectItem) => this.actor.activeOption && this.actor.activeOption.id === option.id);
        this.actor.activeOption = this.actor.options[indexParent].children[index + 1];
        if (!this.actor.activeOption) {
            if (this.actor.options[indexParent + 1]) {
                this.actor.activeOption = this.actor.options[indexParent + 1].children[0];
            }
        }
        if (!this.actor.activeOption) {
            this.first();
        }
        this.fillOptionsMap();
        this.ensureHighlightVisible(this.optionsMap);
    }

    public filter(query: RegExp): void {
        let options: Array<SelectItem> = [];
        let optionsMap: Map<string, number> = new Map<string, number>();
        let startPos = 0;
        for (let si of this.actor.itemObjects) {
            let children: Array<SelectItem> = si.children.filter((option: SelectItem) => {
                if (this.queryLike) {
                    return query.test(option.text)
                } else {
                    return option.text.toLowerCase().startsWith(query.source.toLowerCase());
                }
            });
            startPos = fillChildrenHash(si, optionsMap, startPos);
            if (children.length > 0) {
                let newSi = getSimilar(si);
                newSi.children = children;
                options.push(newSi);
            }
        }
        this.actor.options = options;
        if (this.actor.options.length > 0) {
            this.actor.activeOption = this.actor.options[0].children[0];
            super.ensureHighlightVisible(optionsMap);
        }
    }
}


