

export function isManagedError(errCode) {
    let s: string = errCode.toString();
    console.log("managed? : " + s);
    if (errCode && s.match("managed")) {
        return true;
    }
    return false;
}

export function cleanMessageList(actual) {
    var newArray = new Array();
    for (var i = 0; i < actual.length; i++) {
        if (newArray.indexOf(actual[i].trim())<0) {
            newArray.push(actual[i].trim());
        }
    }
    return newArray;
}
