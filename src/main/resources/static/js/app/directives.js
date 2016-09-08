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
        require: 'kendoGrid',
        controller: [function(){
            var ctrl = this;
            ctrl.options = {
                            dataSource: null,/*{
                                type: "odata",
                                transport: {
                                    read: "//demos.telerik.com/kendo-ui/service/Northwind.svc/Employees"
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
                                console.log(this);
                                this.expandRow(this.tbody.find("tr.k-master-row").first());
                            },
                            height: 453,
                            groupable: false,
                            resizable: true,
                            reorderable: false,
                            columnMenu: true,
                            columns: [{
                                field: "FirstName",
                                title: "First Name",
                                width: "120px"
                                },{
                                field: "LastName",
                                title: "Last Name",
                                width: "120px"
                                },{
                                field: "Country",
                                width: "120px"
                                },{
                                field: "City",
                                width: "120px"
                                },{
                                field: "GroupTitle",
                                columns:[{
                                    field: "Title",
                                }]
                            }]
                        };
        }],
        controllerAs: 'imsgrid'
    };
}])