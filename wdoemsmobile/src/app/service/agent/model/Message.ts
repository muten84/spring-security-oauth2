export interface Message {
    id?: string;
    from?: string;
    type?: string;
    to?: string;
    timestamp?: number;
    payload?: string; //json stringify object

    fromUrl?: string;
    processed?: boolean;
    relatesTo?: string;
    rpcOperation?: string;
    ttl?: number;
    asynch: boolean;


    /*  public get fromUrl() {
         return this._fromUrl;
     }
 
     public set fromUrl(value: string) {
         this._fromUrl = value;
     }
 
     public get processed() {
         return this._processed;
     }
 
     public set processed(value: boolean) {
         this._processed = value;
     }
 
     public get relatesTo() {
         return this._relatesTo;
     }
 
     public set relatesTo(value: string) {
         this._relatesTo = value;
     }
 
     public get rpcOperation() {
         return this._rpcOperation;
     }
 
     public set rpcOperation(value: string) {
         this._rpcOperation = value;
     }
 
     public set ttl(value: number) {
         this._ttl = value;
     }
 
     public get ttl() {
         return this._ttl;
     }
 
     public get id(): string {
         return this._id;
     }
 
     public set id(value: string) {
         this._id = value;
     }
 
     public get type(): string {
         return this._type;
     }
 
     public set type(value: string) {
         this._type = value;
     }
 
     public get from(): string {
         return this._from;
     }
 
     public set from(value: string) {
         this._from = value;
     }
 
     public get to(): string {
         return this._to;
     }
 
     public set to(value: string) {
         this._to = value;
     }
 
     public get timestamp(): number {
         return this._timestamp;
     }
 
     public set timestamp(value: number) {
         this._timestamp = value;
     }
 
     public get payload(): any {
         return this._payload;
     }
 
     public set payload(value: any) {
         this._payload = value;
     } */
}