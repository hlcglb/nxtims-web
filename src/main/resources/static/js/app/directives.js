/*
 * AngularJS Directive 설정 js
 *
 * @author 정철균
 */
'use strict';

angular.module('nxtims.directives',[]);
angular.module('nxtims.directives')
.directive('imsGrid', [function() {
    
    return {
        restrict: 'A',
        controller: ['$element', function(element){
            var ctrl = this;
            ctrl.options = {
                            editable: true,
                            selectable: true,
                            sortable: true,
                            pageable: true,
                            dataBound: function() {
                                this.select("tr:eq(0)");
                            },
                            height: 453,
                            groupable: false,
                            resizable: true,
                            reorderable: false,
                            columnMenu: true
                        };
        }],
        controllerAs: 'imsgrid',
        link: function(scope, element, attrs, ctrl){

        }
    };
}])