/*
 * AngularJS Directive 설정 js
 *
 * @author 정철균
 */

angular.module('nxtims.directives',[]);
angular.module('nxtims.directives')
.directive('imsGrid', [function() {
    return {
        restrict: 'A',
        controller: ['$element', function(element){
            var ctrl = this;
            ctrl.options = {
                            dataSource: null,/*{
                                type: "odata",
                                transport: {
                                    read: "//demos.telerik.com/kendo-ui/service/Northwind.svc/Employees"
                                    parameterMap: function(data, type){
                                    }
                                },
                                pageSize: 7,
                                serverPaging: true,
                                serverSorting: true
                            },*/
                            editable: true,
                            selectable: "row",
                            sortable: true,
                            pageable: true,
                            dataBound: function() {
                                this.expandRow(this.tbody.find("tr.k-master-row").first());
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