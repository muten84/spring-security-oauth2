import { EventEmitter, HostBinding, Directive, ElementRef, HostListener, Input, Output } from '@angular/core';
/*import { MdDialog, MdDialogRef } from '@angular/material';*/
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { VirtualKeyboardComponent } from './virtual-keyboard.component';
import {
  alphanumericKeyboard,
  alphanumericNordicKeyboard,
  extendedKeyboard,
  extendedKeyboardConfirm,
  extendedNordicKeyboard,
  KeyboardLayout,
  numericKeyboard,
  phoneKeyboard
} from './layouts';

@Directive({
  selector: '[ng-virtual-keyboard]'
})

export class NgVirtualKeyboardDirective {
  private dialogRef: MatDialogRef<VirtualKeyboardComponent>;
  private opened = false;
  private focus = true;
  private target = undefined;
  private slayout;

  @Input('ng-virtual-keyboard-layout') layout: KeyboardLayout | string;
  @Input('ng-virtual-keyboard-placeholder') placeholder: string;
  _items: Array<any>;
  @Output('keyboardClosed')
  keyboardClosed = new EventEmitter<any>();

  @Output('ng-virtual-keyboard-item-selected')
  itemSelected = new EventEmitter<any>();

  @Output('ng-virtual-keyboard-requestItems')
  requestItems = new EventEmitter<any>();

  requestingItems;

  /* _proceedOpen: boolean = true; */

  @HostListener('window:blur')
  onWindowBlur() {
    this.focus = false;
  }


  @HostListener('document:OpenKeyboardEvent', ['$event', "$event.detail.origin", "$event.detail.keyboardType", "$event.detail.target", "$event.detail.items"])
  onKeyboardOpen(event, origin, layout, target, items) {

    if (origin === this.element.nativeElement.id) {
      this.layout = layout;
      this.target = target;
      this.openKeyboard(items);
    }
  }

  @HostListener('window:focus')
  onWindowFocus() {
    setTimeout(() => {
      this.focus = true;
    }, 0);
  }

  @HostListener('focus')
  onFocus() {
    if (event) {
      //console.log("keyoboard on click: " + event);
    }
    if (this.items) {
      //console.log("keyoboard on click: " + this.items.length);
      this.openKeyboard(this.items);
    }
    else {
      this.openKeyboard();
    }
  }

  @HostListener('click', ['$event'])
  onClick(event) {
    if (event) {
      //console.log("keyoboard on click: " + event);
    }
    if (this.items) {
      //console.log("keyoboard on click: " + this.items.length);
      this.openKeyboard(this.items);
    }
    else {
      this.openKeyboard();
    }
  }

  /**
   * Constructor of the class.
   *
   * @param {ElementRef}  element
   * @param {MdDialog}    dialog
   */
  public constructor(
    private element: ElementRef,
    private dialog: MatDialog,
  ) {
    this.target = element.nativeElement.name;
  }

  /**
   * Method to open virtual keyboard
   */
  private openKeyboard(items?) {
    /*  if (!this._proceedOpen) {
       return;
     } */
    if (!this.opened && this.focus) {
      this.opened = true;



      this.dialogRef = this.dialog.open(VirtualKeyboardComponent);
      this.dialogRef.componentInstance.inputElement = this.element;
      this.dialogRef.componentInstance.layout = this.getLayout();
      this.dialogRef.componentInstance.slayout = this.slayout;
      this.dialogRef.componentInstance.placeholder = this.getPlaceHolder();
      //dialogRef.componentInstance.items = [{ description: "ELEMENTO 1" }, { description: "ELEMENTO 2" }, { description: "ELEMENTO 3" }, { description: "ELEMENTO 4" }, { description: "ELEMENTO 5" }]
      this.dialogRef.componentInstance.items = items;
      this.requestItems.emit('empty');
      this.dialogRef.componentInstance.onUpdateItems.subscribe((event) => {
        if (this.requestingItems) {
          clearTimeout(this.requestingItems);
        }
        this.requestingItems = setTimeout(() => {
          this.requestItems.emit(event);
        }, 500)
      });
      this.dialogRef.disableClose = true;

      this.dialogRef.beforeClose().subscribe(() => {
        this.itemSelected.emit(this.dialogRef.componentInstance.currentItem);
        this.keyboardClosed.emit({ target: this.target, value: this.dialogRef.componentInstance.inputElement.nativeElement.value });
      });


      this.dialogRef
        .afterClosed()
        .subscribe((event) => {
          //console.log("dialogClosed", event);

          setTimeout(() => {
            this.opened = false;
            //this.keyboardClosed.emit({ target: this.target, value: dialogRef.componentInstance.inputElement.nativeElement.value });
          }, 0);
        });
    }
  }

  /**
   * Getter for used keyboard layout.
   *
   * @returns {KeyboardLayout}
   */
  private getLayout(): KeyboardLayout {
    let layout;
    

    switch (this.layout) {
      case 'alphanumeric':
        layout = alphanumericKeyboard;
        this.slayout = "alphanumeric";
        break;
      case 'alphanumericNordic':
        layout = alphanumericNordicKeyboard;
        this.slayout = "alphanumericNordic";
        break;
      case 'extended':
        layout = extendedKeyboard;
        this.slayout = "extended";
        break;
      case 'extendedConfirm':
        layout = extendedKeyboardConfirm;
        this.slayout = "extendedConfirm";
        break;
      case 'extendedNordic':
        layout = extendedNordicKeyboard;
        this.slayout = "extendedNordic";
        break;
      case 'numeric':
        layout = numericKeyboard;
        this.slayout = "numeric";
        break;
      case 'phone':
        layout = phoneKeyboard;
        this.slayout = "phone";
        break;
      default:
        layout = this.layout;
        this.slayout = "";
        break;
    }

    return layout;
  }

  /**
   * Getter for used placeholder for virtual keyboard input field.
   *
   * @returns {string}
   */
  private getPlaceHolder(): string {
    return this.placeholder ? this.placeholder : this.element.nativeElement.placeholder;
  }

  get items() {
    return this._items;
  }

  @Input('ng-virtual-keyboard-items') set items(value: string | any[]) {
    //console.log("keyboard processing items...");
    if (!this.dialogRef || !this.dialogRef.componentInstance) {
      return;
    }
    if (typeof value == 'string') {
      this._items = JSON.parse(value)
    } else {
      this._items = value
    }
    if (this.dialogRef.componentInstance) {
      this.dialogRef.componentInstance.items = this._items;
    }
  }

  /*  @Input('ng-virtual-keyboard-proceedOpen') set proceedOpen(proceed: boolean) {
     this._proceedOpen = proceed;
   }
 
   get proceedOpen() {
     return this._proceedOpen;
   } */


}
