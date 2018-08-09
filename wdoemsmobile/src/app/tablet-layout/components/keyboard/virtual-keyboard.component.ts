import { EventEmitter, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LocalBusService, ResourceListService, ApplicationStatusService} from '../../../service/service.module';

import { keyboardCapsLockLayout, KeyboardLayout, extendedKeyboard, extendedKeyboardConfirm } from './layouts';
import { VirtualKeyboardService } from './virtual-keyboard.service';
import { KeyPressInterface } from './key-press.interface';

@Component({
  selector: 'virtual-keyboard',
  template: `
  
    <div class="keyboard-container">
    
      <div fxLayout="column" style="margin-bottom:10px;">
        <mat-input-container>
          <button class="close"  color="primary" mat-mini-fab [disabled]="disCheck"  [hidden]="getHiddenCheck()"
            (click)="close()"
          >
            <mat-icon>check</mat-icon>
          </button>

          <button class="close" style="margin-right: 5px;background-color: #eb1b4c" mat-mini-fab
          (click)="clean()"
        >
          <mat-icon>cancel</mat-icon>
        </button>
    
          <input type="text"
            matInput
            readonly="true"
            #keyboardInput
            (click)="updateCaretPosition()"
            [(ngModel)]="inputElement.nativeElement.value" (change)="updateItems($event)" placeholder="{{ placeholder }}"
            [maxLength]="maxLength"
          />
        </mat-input-container>
        <mat-dialog-content *ngIf="items && items.length>0">
          <mat-grid-list cols="6" rowHeight="5rem" >
          <mat-grid-tile [ngClass]="{'mat-grid-tile-active': currentItem?.description === item?.description}" (click)="itemSelected(item)"
              *ngFor="let item of items">{{item.description}}</mat-grid-tile>
        </mat-grid-list>
      </mat-dialog-content>
        <div fxLayout="row" fxLayoutAlign="center center"
          *ngFor="let row of layout; let rowIndex = index"
          [attr.data-index]="rowIndex" 
        >
          <virtual-keyboard-key
            *ngFor="let key of row; let keyIndex = index"
            [key]="key"
            [disabled]="disabled"
            [attr.data-index]="keyIndex"
            (keyPress)="keyPress($event)"
          ></virtual-keyboard-key>
        </div>
      </div>
    </div>
  `,
  styles: [`
  .mat-raised-button.mat-primary .mat-fab.mat-primary {
    background-color: #077480 !important;
  }

  .keyboard-container {
    padding-top: 19px;
    padding-left: 21px;
    padding-right: 25px;
  }
  .close{
      position: relative;
      float: right;
      top: -16px;
      right: 0;
      margin-bottom: -40px;
    //  left: 24px;
  }
   /* .close {
      position: relative;
      float: right;
      top: -16px;
      right: 0;
      margin-bottom: -40px;
    }*/
  
    .mat-input-container {
      margin: -16px 0;
      font-size: 32px;
    }
  
    .mat-input-element:disabled {
      color: currentColor;
    }

    .mat-form-field-infix {
      top: 15px !important;
    }

    .mat-input-placeholder {
      top: 10px !important;     
      font-size: 24px !important; 
    }

    .mat-form-field-placeholder-wrapper {      
      font-size: 24px !important; 
    }
    
    .mat-input-container{
      font-size: 24px !important; 
    }

    .mat-grid-tile {
      background: #072c3f;
      color: #fff;
      text-align: center !important;
  }
  
  .mat-grid-tile-active {
      background-color: #006b84 !important;
  }
  
  .mat-grid-tile:hover {
      background-color: #006b84 !important;
  }
  
  .mat-grid-tile:active {
      background-color: #006b84 !important;
  }
  
  .mat-grid-tile:focus {
      background-color: #006b84 !important;
  } 

  .mat-mini-fab{
    width: 50px !important;
    height: 50px !important;
  }

  .mat-icon {
     background-repeat: no-repeat; 
     display: inline-block; 
     fill: currentColor; 
     width: 50px !important;
     height: 50px !important;
    
  }

  .material-icons {
      font-family: 'Material Icons';
      font-weight: normal;
      font-style: normal;
      font-size: 36px !important;
      line-height: 1;
      letter-spacing: normal;
      text-transform: none;
      display: inline-block;
      white-space: nowrap;
      word-wrap: normal;
      direction: ltr;
      -webkit-font-feature-settings: 'liga';
      -webkit-font-smoothing: antialiased;
  }

  `]
})

export class VirtualKeyboardComponent implements OnInit, OnDestroy {
  @ViewChild('keyboardInput') keyboardInput: ElementRef;

  public inputElement: ElementRef;
  public layout: KeyboardLayout;
  public slayout: string;
  public placeholder: string;
  public disabled: boolean;
  public maxLength: number | string;

  private caretPosition: number;
  private shift = false;

  public items = [];
  public currentItem;

  public onUpdateItems = new EventEmitter();
  public onItemSelected = new EventEmitter();
  public disCheck :boolean = false;

  /**
   * Helper method to set cursor in input to correct place.
   *
   * @param {HTMLInputElement|HTMLTextAreaElement}  input
   * @param {number}                                start
   * @param {number}                                end
   */
  private static setSelectionRange(
    input: any,
    start: number,
    end: number
  ): void {
    if (input.setSelectionRange) {
      input.focus();
      input.setSelectionRange(start, end);

    } else if (input.createTextRange) {
      const range = input.createTextRange();

      range.collapse(true);
      range.moveEnd('character', end);
      range.moveStart('character', start);
      range.select();
    }
  }

  /**
   * Constructor of the class.
   *
   * @param {MdDialogRef<VirtualKeyboardComponent>} dialogRef
   * @param {VirtualKeyboardService}                virtualKeyboardService
   */
  public constructor(
    public dialogRef: MatDialogRef<VirtualKeyboardComponent>,
    private virtualKeyboardService: VirtualKeyboardService,
    private bus: LocalBusService,
    private resourceService: ResourceListService,
    private appStatus: ApplicationStatusService
  ) { }

  /**
   * On init life cycle hook, this will do following:
   *  1) Set focus to virtual keyboard input field
   *  2) Subscribe to following
   *    2.1) Shift key, this is needed in keyboard event dispatches
   *    2.2) CapsLock key, this will change keyboard layout
   *    2.3) Caret position in virtual keyboard input
   *  3) Reset of possible previously tracked caret position
   */
  public ngOnInit(): void {
    setTimeout(() => {
      this.keyboardInput.nativeElement.focus();
    }, 0);

    this.virtualKeyboardService.shift$.subscribe((shift: boolean) => {
      this.shift = shift;
    });

    this.virtualKeyboardService.capsLock$.subscribe((capsLock: boolean) => {
      this.layout = keyboardCapsLockLayout(this.layout, capsLock);
    });

    this.virtualKeyboardService.caretPosition$.subscribe((caretPosition: number) => {
      this.caretPosition = caretPosition;

      setTimeout(() => {
        VirtualKeyboardComponent.setSelectionRange(this.keyboardInput.nativeElement, caretPosition, caretPosition);
      }, 0);
    });

    if (this.inputElement.nativeElement.value.length) {
      this.virtualKeyboardService.setCaretPosition(this.inputElement.nativeElement.value.length);
    }

    this.maxLength = this.inputElement.nativeElement.maxLength > 0 ? this.inputElement.nativeElement.maxLength : '';

    this.checkDisabled();
    this.onUpdateItems.emit(this.inputElement.nativeElement.value);
  }

  /**
   * On destroy life cycle hook, in this we want to reset virtual keyboard service states on following:
   *  - Shift
   *  - CapsLock
   */
  public ngOnDestroy(): void {
    this.virtualKeyboardService.reset();
  }

  /**
   * Method to close virtual keyboard dialog
   */
  public close(): void {
    this.dialogRef.close(this.currentItem);
  }

  public clean(): void {

    this.inputElement.nativeElement.value = "";
    this.currentItem = "";
    this.updateItems(this.inputElement.nativeElement.value);
    this.setDisCheck();    
  }


  /**
   * Method to update caret position. This is called on click event in virtual keyboard input element.
   */
  public updateCaretPosition(): void {
    this.virtualKeyboardService.setCaretPosition(this.keyboardInput.nativeElement.selectionStart);
  }

  /**
   * Method to handle actual "key" press from virtual keyboard.
   *  1) Key is "Special", process special key event
   *  2) Key is "Normal"
   *    - Append this key value to input
   *    - Dispatch DOM events to input element
   *    - Toggle Shift key if it's pressed
   *
   * @param {KeyPressInterface} event
   */
  public keyPress(event: KeyPressInterface): void {
    //console.log("keyPress: " + event.keyValue);
    if (event.special) {
      this.handleSpecialKey(event);
    } else {
      this.handleNormalKey(event.keyValue);

      this.dispatchEvents(event);

      // Toggle shift if it's activated
      if (this.shift) {
        this.virtualKeyboardService.toggleShift();
      }
    }
    this.updateItems(event);

    this.checkDisabled();
    this.setDisCheck();
  }

  /**
   * Method to check is virtual keyboard input is disabled.
   */
  private checkDisabled(): void {
    const maxLength = this.inputElement.nativeElement.maxLength;
    const valueLength = this.inputElement.nativeElement.value.length;

    this.disabled = maxLength > 0 && valueLength >= maxLength;
  }

  /**
   * Method to handle "normal" key press event, this will add specified character to input value.
   *
   * @param {string}  keyValue
   */
  private handleNormalKey(keyValue: string): void {
    let value = '';

    // We have caret position, so attach character to specified position
    if (!isNaN(this.caretPosition)) {
      value = [
        this.inputElement.nativeElement.value.slice(0, this.caretPosition),
        keyValue,
        this.inputElement.nativeElement.value.slice(this.caretPosition)
      ].join('');

      // Update caret position
      this.virtualKeyboardService.setCaretPosition(this.caretPosition + 1);
    } else {
      value = `${this.inputElement.nativeElement.value}${keyValue}`;
    }

    // And finally set new value to input
    this.inputElement.nativeElement.value = value;
  }

  /**
   * Method to handle "Special" key press events.
   *  1) Enter
   *  2) Escape, close virtual keyboard
   *  3) Backspace, remove last character from input value
   *  4) CapsLock, toggle current layout state
   *  6) Shift, toggle current layout state
   *  5) SpaceBar
   */
  private handleSpecialKey(event: KeyPressInterface): void {
    switch (event.keyValue) {
      case 'Enter':
        this.close();
        break;
      case 'Escape':
        this.close();
        break;
      case 'Backspace':
        const currentValue = this.inputElement.nativeElement.value;

        // We have a caret position, so we need to remove char from that position
        if (!isNaN(this.caretPosition)) {
          // And current position must > 0
          if (this.caretPosition > 0) {
            const start = currentValue.slice(0, this.caretPosition - 1);
            const end = currentValue.slice(this.caretPosition);

            this.inputElement.nativeElement.value = `${start}${end}`;

            // Update caret position
            this.virtualKeyboardService.setCaretPosition(this.caretPosition - 1);
          }
        } else {
          this.inputElement.nativeElement.value = currentValue.substring(0, currentValue.length - 1);
        }
        this.setDisCheck();
        // Set focus to keyboard input
        this.keyboardInput.nativeElement.focus();
        break;
      case 'CapsLock':
        this.virtualKeyboardService.toggleCapsLock();
        break;
      case 'Shift':
        this.virtualKeyboardService.toggleShift();
        break;
      case 'Conferma':
        if(!this.disCheck){
          this.close();
        }
        break;
      case 'SpaceBar':
        this.handleNormalKey(' ');
        break;
    }
  }

  /**
   * Method to dispatch necessary keyboard events to current input element.
   *
   * @see https://w3c.github.io/uievents/tools/key-event-viewer.html
   *
   * @param {KeyPressInterface} event
   */
  private dispatchEvents(event: KeyPressInterface) {
    const eventInit: KeyboardEventInit = {
      bubbles: true,
      cancelable: true,
      shiftKey: this.shift,
      key: event.keyValue,
      code: `Key${event.keyValue.toUpperCase()}}`,
      location: 0
    };

    // Simulate all needed events on base element
    this.inputElement.nativeElement.dispatchEvent(new KeyboardEvent('keydown', eventInit));
    this.inputElement.nativeElement.dispatchEvent(new KeyboardEvent('keypress', eventInit));
    this.inputElement.nativeElement.dispatchEvent(new Event('input', { bubbles: true }));
    this.inputElement.nativeElement.dispatchEvent(new KeyboardEvent('keyup', eventInit));

    // And set focus to input
    this.keyboardInput.nativeElement.focus();
  }

  itemSelected(item) {
    //console.log("keyboard item selected: " + item);
    this.inputElement.nativeElement.value = item.description;
    this.currentItem = item;
    this.updateCaretPosition();
    if (this.inputElement.nativeElement.value.length) {
      this.virtualKeyboardService.setCaretPosition(this.inputElement.nativeElement.value.length);
      this.disCheck = false;
    }else{
      this.updateItems(this.inputElement.nativeElement.value); 
      this.setDisCheck();      
    }
  }

  updateItems(event) {
    //console.log("update items: " + event);
    this.onUpdateItems.emit(this.inputElement.nativeElement.value);
  }

  private showInfoDialog(title, text) {
    this.bus.notifyAll('dialogs', { type: 'messageDialog', payload: { width: '100px', title: title, content: text } });
  }

  private setDisCheck(){
    if(this.items && this.items.length > 0){
      this.disCheck =  this.inputElement.nativeElement.value && this.inputElement.nativeElement.value.length > 0?true:false;
    }
  }

  getHiddenCheck(){   
    return true;//this.slayout === "extendedConfirm";
  }

}
