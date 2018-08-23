import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

import { KeyPressInterface } from './key-press.interface';
import { isSpacer, isSpecial, notDisabledSpecialKeys, specialKeyIcons, specialKeyTexts } from './layouts';

@Component({
  selector: 'virtual-keyboard-key',
  template: `
    <button *ngIf="icon != 'check'"
      mat-raised-button
      color="primary"
      fxFlex="{{ flexValue }}"
      [class.spacer]="spacer"
      [disabled]="isDisabled()"
      (click)="onKeyPress()"
    >
      <span *ngIf="!special">{{ keyValue }}</span>
    
      <span *ngIf="special">
        <mat-icon *ngIf="icon">{{ icon }}</mat-icon>
    
        {{ text }}
      </span>
    </button>
  
  <button class="close" (click)="onKeyPress()" *ngIf="special && icon === 'check'"  color="primary" mat-mini-fab  
    style="background-color:green;border: 0px;margin-left:20px;"
   >
    <mat-icon>check</mat-icon>
  </button>
  `,
  styles: [`
    .mat-button,
    .mat-icon-button,
    .mat-raised-button {
      min-width: 64px;
      min-height: 64px;
      padding: 0;
      margin: 2px;
      font-size: 32px;
      line-height: 32px;      
    }


    .mat-primary {
      background-color: #077480;
    }

    .mat-button.primary,
    .mat-icon-button.primary,
    .mat-raised-button.primary {
      background-color: #077480;
    }

    .mat-fab,
    .mat-mini-fab,
    .mat-primary {
      background-color: #077480;
  }
    
    .mat-button.spacer,
    .mat-icon-button.spacer,
    .mat-raised-button.spacer {
      background-color: transparent;
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


  `]
})

export class VirtualKeyboardKeyComponent implements OnInit {
  @Input() key: string;
  @Input() disabled: boolean;
  @Output() keyPress = new EventEmitter<KeyPressInterface>();

  public special = false;
  public spacer = false;
  public flexValue: string;
  public keyValue: string;
  public icon: string;
  public text: string;

  /**
   * Constructor of the class.
   */
  public constructor() { }

  /**
   * On init life cycle hook, within this we'll initialize following properties:
   *  - special
   *  - keyValue
   *  - flexValue
   */
  public ngOnInit(): void {
    let multiplier = 1;
    let fix = 0;

    if (this.key.length > 1) {
      this.spacer = isSpacer(this.key);
      this.special = isSpecial(this.key);

      const matches = /^(\w+)(:(\d+(\.\d+)?))?$/g.exec(this.key);

      this.keyValue = matches[1];

      if (matches[3]) {
        multiplier = parseFloat(matches[3]);
        fix = (multiplier - 1) * 4;
      }
    } else {
      this.keyValue = this.key;
    }

    if (this.special) {
      if (specialKeyIcons.hasOwnProperty(this.keyValue)) {
        this.icon = specialKeyIcons[this.keyValue];
      } else if (specialKeyTexts.hasOwnProperty(this.keyValue)) {
        this.text = specialKeyTexts[this.keyValue];
      }
    }

    this.flexValue = `${multiplier * 64 + fix}px`;
  }

  /**
   * Method to check if key is disabled or not.
   *
   * @returns {boolean}
   */
  public isDisabled(): boolean {
    if (this.spacer) {
      return true;
    } else if (this.disabled && notDisabledSpecialKeys.indexOf(this.keyValue) !== -1) {
      return false;
    } else {
      return this.disabled;
    }
  }

  /**
   * Method to handle actual "key" press from virtual keyboard.
   *  1) Key is "Special", process special key event
   *  2) Key is "Normal", append this key value to input
   */
  public onKeyPress(): void {
    this.keyPress.emit({ special: this.special, keyValue: this.keyValue, key: this.key });
  }
}
