export enum Type {
    token,
    filter,
    request,
    startPopup,
    navigate, ADD_BOOKING_TO_SERVICE,
    APP2_SET_CURRENT_BOOKING,
    APP2_SET_CURRENT_SERVICE,
    APP2_REMOVE_BOOKING_AND_UNMASK,
    APP2_GET_SELECTED_BOOKING

}


export interface Message {
    type?: Type;
}

export interface RequestMessage extends Message {
    parameters?: string[];
}

export interface NavigateMessage extends RequestMessage {
    path: string;
}

export interface StartMessage extends Message {
    token: string;
    path: string;
}

export interface AddBookingToServiceMessage extends Message {
    bookingId: string;
    serviceId: string;
}

export interface SelectServiceInSinottico extends Message {
    serviceId: string,
    serviceCode: string
}

export interface SelectBookingInSinottico extends Message {
    bookingId: string,
    bookingCode: string
}

