import { Output,Component,Input,EventEmitter } from '@angular/core';
import {UserInfo,RightSideItem} from '../model/topnav-item';

@Component({
  selector: 'top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.css']
})
export class TopNavComponent  {

  @Input() appName: string;
  @Input() appVersion: string;
  @Input() userInfo: UserInfo;
  @Input() rightSideItems: RightSideItem[];

  @Output() rightSideClick= new EventEmitter();

  constructor() { }

  public onItemClick(data, i){
    //console.log("onItemClick: "+data+ " - "+i);
    this.rightSideClick.emit(i);
  }
 
}