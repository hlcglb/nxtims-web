angular.module('nxtims.filters', [])
.filter('nospace', function () {
    return function (value) {
        console.log(value);
        return (!value) ? '' : value.replace(/ /g, '');
    };
});