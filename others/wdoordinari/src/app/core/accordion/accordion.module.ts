import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Accordion, Panel, PanelTitle, PanelContent, PanelChangeEvent } from './accordion';
import { AccordionConfig } from './accordion-config';
import { AccordionHeader } from './accordion-header';

export { Accordion, Panel, PanelTitle, PanelContent, PanelChangeEvent } from './accordion';
export { AccordionHeader } from './accordion-header';
export { AccordionConfig } from './accordion-config';

const _ACCORDION_DIRECTIVES = [Accordion, AccordionHeader, Panel, PanelTitle, PanelContent];

@NgModule({ declarations: _ACCORDION_DIRECTIVES, exports: _ACCORDION_DIRECTIVES, imports: [CommonModule], providers: [AccordionConfig] })
export class AccordionModule {
  static forRoot(): ModuleWithProviders { return { ngModule: AccordionModule, providers: [AccordionConfig] }; }
}
