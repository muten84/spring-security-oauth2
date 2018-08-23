export interface CurrentTurn {

    resource: string;

    startTurnDate: Date;

    duration: number;

    endturnDate?: Date;

    /*  private _resource: string;
  
      private _startTurnDate: Date;
  
      private _duration: number;
  
      public get resource(): string {
          return this._resource;
      }
  
      public set resource(value: string) {
          this._resource = value;
      }
  
      public get duration(): number {
          return this._duration;
      }
  
      public set duration(value: number) {
          this._duration = value;
      }
  
      public get startTurnDate(): Date {
          return this._startTurnDate;
      }
  
      public set startTurnDate(value: Date) {
          this._startTurnDate = value;
      }
  
      public toJsonString(): string {
          let json = JSON.stringify(this);
          Object.keys(this).filter(key => key[0] === "_").forEach(key => {
              json = json.replace(key, key.substring(1));
          });
          return json;
      }*/
}