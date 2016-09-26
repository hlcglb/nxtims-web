/*
 * AngularJS Directive 설정 js
 *
 * @author 정철균
 */
'use strict';

angular.module('nxtims.directives',[]);
/**
 * ims common grid optins
 */
angular.module('nxtims.directives')
.directive('imsGrid', [function() {
    function attachDblClickToRows(scope, element, attrs) {
        /**
         * event bind - grid <tr> double click
         * @example <kendo-grid k-on-grid-row-dblclick="functionName(rowIndex, rowData)" ></kendo-grid>
         */
        if(attrs.kOnGridRowDblclick){
            element.find('tbody tr').on('dblclick', function(event) {
                var rowScope = angular.element(event.currentTarget).scope();
                var currIdx = element.getKendoGrid().dataSource.indexOf(rowScope.dataItem);
                scope.$eval(attrs.kOnGridRowDblclick, {
                    rowIndex: currIdx,
                    rowData: rowScope.dataItem
                });
            });
        }
        //event bind - grid <tr> click
        if(attrs.kOnGridRowClick || attrs.kOnGridRowChange){
            element.find('tbody tr').on('click', function(event) {;
                var rowScope = angular.element(event.currentTarget).scope();
                var grid = element.getKendoGrid();
                var oldIdx = grid.select().index();
                var oldRowData = grid.dataItem(grid.select());
                var currIdx = grid.dataSource.indexOf(rowScope.dataItem);

                if(oldIdx == currIdx) return; //같은 로우이면 리턴
                /**
                 * row click event 
                 * @example <kendo-grid k-on-grid-row-click="functionName(rowIndex, rowData)" ></kendo-grid>
                 */
                if(attrs.kOnGridRowClick){
                    scope.$eval(attrs.kOnGridRowClick, {
                        rowIndex: currIdx,
                        rowData: rowScope.dataItem
                    });
                }
                /**
                 * row change event 
                 * @example <kendo-grid k-on-grid-row-click="functionName(oldRow, oldData, currRow, currData)"></kendo-grid>
                 */
                if(attrs.kOnGridRowChange){

                    scope.$eval(attrs.kOnGridRowChange, {
                        oldRow: oldIdx,
                        oldData: oldRowData,
                        currRow: currIdx,
                        currData: rowScope.dataItem
                    });
                }
            });
        }
        
    };
    return {
        restrict: 'A',
        controller: ['$scope', '$element', function($scope, element){
            var ctrl = this;
            ctrl.options = {
                            editable: true,
                            selectable: true,
                            sortable: true,
                            pageable: true,
                            height: 453,
                            groupable: false,
                            resizable: true,
                            reorderable: false,
                            columnMenu: true
                        };
        }],
        controllerAs: 'imsgrid',
        link: function(scope, element, attrs, ctrl){
            scope.$on("kendoWidgetCreated", function(event, widget) {
                element.getKendoGrid().bind('dataBound', function () {
                    this.select("tr:eq(0)");
                    attachDblClickToRows(scope, element, attrs);
                });
            });
        }
    };
}]);