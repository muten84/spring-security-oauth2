
angular.module(_FILTERS_).filter('moment', function() {
    return function(date, format) {
        if(moment.isMoment(date))
            return date.format(format);
        else
            return moment(date).format(format);
    };
});

angular.module(_FILTERS_).filter('eventsInMonth', function() {
    return function(events, date) {
        var e = $.grep(events, function(ev){
            var start = moment(ev.startTime);
            return (start.month() == date.month() && start.year() == date.year());
        });
        return e;
    };
});