import {
  AfterContentChecked,
  Component,
  ContentChild,
  ContentChildren,
  Directive,
  EventEmitter,
  Input,
  Output,
  QueryList,
  TemplateRef
} from '@angular/core';

import { isString } from './util';

import { AccordionConfig } from './accordion-config';

let nextId = 0;

/**
 * This directive should be used to wrap accordion panel titles that need to contain HTML markup or other directives.
 */
@Directive({ selector: 'ng-template[ngbootPanelTitle]' })
export class PanelTitle {
  constructor(public templateRef: TemplateRef<any>) { }
}

/**
 * This directive must be used to wrap accordion panel content.
 */
@Directive({ selector: 'ng-template[ngbootPanelContent]' })
export class PanelContent {
  constructor(public templateRef: TemplateRef<any>) { }
}

/**
 * The NgbPanel directive represents an individual panel with the title and collapsible
 * content
 */
@Directive({ selector: 'ngboot-panel' })
export class Panel {
  /**
   *  A flag determining whether the panel is disabled or not.
   *  When disabled, the panel cannot be toggled.
   */
  @Input() disabled = false;

  /**
   *  An optional id for the panel. The id should be unique.
   *  If not provided, it will be auto-generated.
   */
  @Input() id = `ngboot-panel-${nextId++}`;

  /**
   *  The title for the panel.
   */
  @Input() title: string;

  @Input() titleIconClass: string;

  /**
   *  Accordion's types of panels to be applied per panel basis.
   *  Bootstrap 4 recognizes the following types: "success", "info", "warning" and "danger".
   */
  @Input() type: string;

  @ContentChild(PanelContent) contentTpl: PanelContent;
  @ContentChild(PanelTitle) titleTpl: PanelTitle;
}

/**
 * The payload of the change event fired right before toggling an accordion panel
 */
export interface PanelChangeEvent {
  /**
   * Id of the accordion panel that is toggled
   */
  panelId: string;

  /**
   * Whether the panel will be opened (true) or closed (false)
   */
  nextState: boolean;

  /**
   * Function that will prevent panel toggling if called
   */
  preventDefault: () => void;
}

/**
 * The NgbAccordion directive is a collection of panels.
 * It can assure that only one panel can be opened at a time.
 */
@Component({
  selector: 'ngboot-accordion',
  exportAs: 'ngbootAccordion',
  host: { 'role': 'tablist', '[attr.aria-multiselectable]': '!closeOtherPanels' },
  template: `
  <div class="card">
    <ng-template ngFor let-panel [ngForOf]="panels">
      <div role="tab" id="{{panel.id}}-header" (click)="headerClick(panel.id)"
        [class]="'card-header ' + (panel.type ? 'card-'+panel.type: type ? 'card-'+type : '')" [class.active]="isOpen(panel.id)">
        <a href (click)="!!onTitleTextClick(panel.id)" [class.text-muted]="panel.disabled" [attr.tabindex]="(panel.disabled ? '-1' : null)"
          [attr.aria-expanded]="isOpen(panel.id)" [attr.aria-controls]="(isOpen(panel.id) ? panel.id : null)"
          [attr.aria-disabled]="panel.disabled">
          <i [ngClass]="panel.titleIconClass" class="fa"></i>    {{panel.title}}
          <!--<ng-template [ngTemplateOutlet]="panel.titleTpl?.templateRef"></ng-template>-->
        </a>
        <a class="pull-right">
            <i class="fa" [ngClass]="getToggleClass(isOpen(panel.id))"></i>
          </a>
      </div>
      <div [hidden]="!isOpen(panel.id)" id="{{panel.id}}" role="tabpanel" [attr.aria-labelledby]="panel.id + '-header'" [attr.aria-expanded]="isOpen(panel.id)" class="card-block collapse show" >
        <ng-template [ngTemplateOutlet]="panel.contentTpl.templateRef"></ng-template>
      </div>
     
    </ng-template>
  </div>
`
})
export class Accordion implements AfterContentChecked {
  /*
  *ngIf="isOpen(panel.id)"
    <div id="{{panel.id}}" role="tabpanel" [attr.aria-labelledby]="panel.id + '-header'" 
             class="card-body {{panel.isOpen ? 'show' : null}}" *ngIf="destroyOnHide || panel.isOpen">
             <ng-template [ngTemplateOutlet]="panel.contentTpl.templateRef"></ng-template>
        </div>*/
  /**
   * A map that stores each panel state
   */
  private _states: Map<string, boolean> = new Map<string, boolean>();

  /**
   * A map that stores references to all panels
   */
  private _panelRefs: Map<string, Panel> = new Map<string, Panel>();

  @ContentChildren(Panel) panels: QueryList<Panel>;

  /**
   * An array or comma separated strings of panel identifiers that should be opened
   */
  @Input() activeIds: string | string[] = [];

  /**
   *  Whether the other panels should be closed when a panel is opened
   */
  @Input('closeOthers') closeOtherPanels: boolean;

  /**
   *  Accordion's types of panels to be applied globally.
   *  Bootstrap 4 recognizes the following types: "success", "info", "warning" and "danger".
   */
  @Input() type: string;

  @Input() iconHeaderClass: string;

  @Input() destroyOnHide: boolean = false;


  /**
   * A panel change event fired right before the panel toggle happens. See NgbPanelChangeEvent for payload details
   */
  @Output() panelChange = new EventEmitter<PanelChangeEvent>();

  /**
     * A panel change event fired right before the panel toggle happens. See NgbPanelChangeEvent for payload details
     */
  @Output() headerclick = new EventEmitter<string>();
  @Output() beforeToggle = new EventEmitter<string>();
  @Output() afterToggle = new EventEmitter<string>();

  constructor(config: AccordionConfig) {
    this.type = config.type;
    this.closeOtherPanels = config.closeOthers;
  }

  headerClick(panelId: string) {
    //console.log("on header click: " + panelId);
    this.toggle(panelId);
    this.headerclick.emit(panelId);
  }

  onTitleTextClick(panelId: string) {
    //console.log("onTitleTextClick" + panelId);
  }

  /**
   * Programmatically toggle a panel with a given id.
   */
  toggle(panelId: string) {
    /*this.beforeToggle.emit(panelId);*/
    const panel = this._panelRefs.get(panelId);

    if (panel && !panel.disabled) {
      const nextState = !this._states.get(panelId);
      let defaultPrevented = false;

      this.panelChange.emit(
        { panelId: panelId, nextState: nextState, preventDefault: () => { defaultPrevented = true; } });

      if (!defaultPrevented) {
        this._states.set(panelId, nextState);

        if (this.closeOtherPanels) {
          this._closeOthers(panelId);
        }
        this._updateActiveIds();
      }
      /*this.afterToggle.emit(panelId);*/
    }
  }

  getToggleClass(isOpen: boolean): string {
    if (isOpen) return 'fa-chevron-up';
    return 'fa-chevron-down';
  }

  ngAfterContentChecked() {
    // active id updates
    if (isString(this.activeIds)) {
      this.activeIds = this.activeIds.split(/\s*,\s*/);
    }
    this._updateStates();

    // closeOthers updates
    if (this.activeIds.length > 1 && this.closeOtherPanels) {
      this._closeOthers(this.activeIds[0]);
      this._updateActiveIds();
    }
  }

  /**
   * @internal
   */
  isOpen(panelId: string): boolean { return this._states.get(panelId); }

  private _closeOthers(panelId: string) {
    this._states.forEach((state, id) => {
      if (id !== panelId) {
        this._states.set(id, false);
      }
    });
  }

  private _updateActiveIds() {
    this.activeIds =
      this.panels.toArray().filter(panel => this.isOpen(panel.id) && !panel.disabled).map(panel => panel.id);
  }

  private _updateStates() {
    this._states.clear();
    this._panelRefs.clear();
    this.panels.toArray().forEach((panel) => {
      this._states.set(panel.id, this.activeIds.indexOf(panel.id) > -1 && !panel.disabled);
      this._panelRefs.set(panel.id, panel);
    });
  }
}
