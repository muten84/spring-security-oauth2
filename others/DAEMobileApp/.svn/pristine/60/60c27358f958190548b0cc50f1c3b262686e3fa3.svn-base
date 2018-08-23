//Filter epochToDate :
//Use for convert epoch date format to default date format.
//Example :
//<p>{{item.createdAt |epochToDate | date:"short"}}</p>
appFilters.filter('epochToDate', function ($filter) {
    return function (input) {
        return new Date(Date(input));
    };
});// End Filter epochToDate.

//Filter numberSuffix :
//Use for convert number to have suffix 1,000 to 1K.
//Example :
//{{item.likes.summary.total_count | numberSuffix}}
//
angular.module(_FILTERS_).filter('numberSuffix', function () {
    return function (input) {
        var exp;
        var suffixes = ['k', 'M', 'G', 'T', 'P', 'E'];

        if (window.isNaN(input)) {
            return 0;
        }

        if (input < 1000) {
            return input;
        }

        exp = Math.floor(Math.log(input) / Math.log(1000));

        return (input / Math.pow(1000, exp)).toFixed(1) + suffixes[exp - 1];
    }
});// End Filter numberSuffix.

angular.module(_FILTERS_).filter('ceil', function() {
    return function(input) {
        return Math.ceil(input);
    };
});

angular.module(_FILTERS_).filter('textToUnit', function() {
    return function(input) {
        if (input === "CHILOMETRI"){
            return "Km"
        }
        if (input === "METRI"){
            return "m"
        }
    };
});