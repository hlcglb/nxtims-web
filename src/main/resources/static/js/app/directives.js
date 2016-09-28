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
.directive('imsGrid', ['$timeout', function($timeout) {
    var delay = 100;
    var prevent = false;
    var cnacel = false;
    var clickPromise;
    function attachDblClickToRows(scope, element, attrs) {
        /**
         * event bind - grid <tr> double click
         * @example <kendo-grid k-on-grid-row-dblclick="functionName(rowIndex, rowData)" ></kendo-grid>
         */
        if(attrs.kOnGridRowDblclick){
            element.find('tbody tr').on('dblclick', function(event) {
                event.preventDefault();
                cnacel = $timeout.cancel(clickPromise);
                var rowScope = angular.element(event.currentTarget).scope();
                var currIdx = element.getKendoGrid().dataSource.indexOf(rowScope.dataItem);
                scope.$eval(attrs.kOnGridRowDblclick, {
                    row: currIdx,
                    dataItem: rowScope.dataItem
                });
            });
        }
        //event bind - grid <tr> click
        if(attrs.kOnGridRowClick || attrs.kOnGridRowChange){
            element.find('tbody tr').on('click', function(event) {
                //event.preventDefault();
                if(!prevent){ //연속이벤트 체크
                    prevent = true;
                    var rowScope = angular.element(event.currentTarget).scope();
                    var grid = element.getKendoGrid();
                    var oldIdx = grid.select().index();
                    var oldRowData = grid.dataItem(grid.select());
                    var currIdx = grid.dataSource.indexOf(rowScope.dataItem);
                    if(oldIdx != currIdx){
                        var time = attrs.delay || delay;
                        clickPromise = $timeout(function(){
                            cnacel = false;
                        }, time, false);
                        clickPromise.then(function(){
                            /**
                             * row can change event 
                             * @example <kendo-grid k-on-grid-row-can-change="functionName(rowIndex, rowData)" ></kendo-grid>
                             */
                            if(attrs.kOnGridRowCanChange){
                                var canChange = scope.$eval(attrs.kOnGridRowCanChange, {
                                    oldRow: oldIdx,
                                    oldData: oldRowData,
                                    currRow: currIdx,
                                    currData: rowScope.dataItem
                                });
                                if(canChange == false){
                                    grid.select("tr:eq(" + oldIdx + ")");
                                    return false;
                                }
                                
                            }
                            /**
                             * row click event 
                             * @example <kendo-grid k-on-grid-row-click="functionName(rowIndex, rowData)" ></kendo-grid>
                             */
                            if(attrs.kOnGridRowClick){
                                scope.$eval(attrs.kOnGridRowClick, {
                                    row: currIdx,
                                    dataItem: rowScope.dataItem
                                });
                            }
                            /**
                             * row change event 
                             * @example <kendo-grid k-on-grid-row-change="functionName(oldRow, oldData, currRow, currData)"></kendo-grid>
                             */
                            if(attrs.kOnGridRowChange){
                                scope.$eval(attrs.kOnGridRowChange, {
                                    row: currIdx,
                                    dataItem: rowScope.dataItem
                                });
                            }
                        }, function(){
                        })
                        .catch(function(error){
                            console.log(error);
                        })
                        .finally(function(){
                            $timeout.cancel(clickPromise);
                            prevent = false;
                        });
                    }
                    else prevent = false;
                    
                }
            });
        }
        
    };
    return {
        restrict: 'A',
        controller: ['$scope', '$element', function($scope, element){
            var ctrl = this;
            var obj = $scope.$eval(element.attr("ims-grid"));
            ctrl.options = {
                            editable: false,
                            selectable: true,
                            sortable: true,
                            /*pageable: {
                                pageSize: 10
                            },*/
                            height: 450,
                            groupable: false,
                            resizable: true,
                            reorderable: false,
                            columnMenu: false,
                            scrollable: true
                        };
            if(obj) {
                angular.extend(ctrl.options, obj);
            }
        }],
        controllerAs: 'imsgrid',
        link: function(scope, element, attrs, ctrl){
            scope.$on("kendoWidgetCreated", function(event, widget) {
                element.getKendoGrid().bind('dataBound', function (e) {
                    var idx;
                    (this.dataSource.isUpdated()) ? idx = this.dataSource.indexOf(this.dataSource.UPDATE[0]) : idx = 0;
                    this.select("tr:eq(" + idx + ")");
                    attachDblClickToRows(scope, element, attrs);
                });
            });
        }
    };
}]);