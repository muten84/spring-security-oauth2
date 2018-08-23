import { ModificaPrenotazioneComponent } from "./modifica-prenotazione.component";
import { CanDeactivate } from "@angular/router";
import { Injectable } from "@angular/core";
import { Observable } from 'rxjs/Observable';
import { ComponentHolderService } from "../service/shared-service";

@Injectable()
export class CanDeactivateBookingModify implements CanDeactivate<ModificaPrenotazioneComponent> {
  constructor(
    private componentService: ComponentHolderService
  ) {

  }
  async canDeactivate(component: ModificaPrenotazioneComponent) {
    let rsp: boolean = await component.hasModificationOnDestroy();
    if (rsp) {
      try {
        let ret = await this.componentService.getRootComponent().showConfirmDialog("Attenzione!", "Sono state effettuate delle modifiche alla prenotazione.\nSi desidera continuare con l'operazione perdendo tali dati?");
        return true;
      } catch (err) {
        this.componentService.getRootComponent().unmask();
        return false;
      }
    }
    return true;
  }
}