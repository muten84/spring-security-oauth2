export interface AckCommand {
    relatesTo: string;
    relatesToType: string;
    result: boolean;
}