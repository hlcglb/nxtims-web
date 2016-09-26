angular.module('nxtims.components',[])

.component('popup', {})
.component('commonCode', {
    template: '<select kendo-drop-down-list k-options="$ctrl.officeOptions"></select>',
    bindings: {
        code: '@',
        text: '@',
        value: '@'
    },
    controller: function(){
        var ctrl = this; 
        ctrl.officeDataSource = {
            transport: {
                read: {
                    dataType: "jsonp",
                    url: "//demos.telerik.com/kendo-ui/service/Customers",
                }
            }
        };
        ctrl.officeOptions = {
            dataSource: ctrl.officeDataSource,
            dataTextField: ctrl.text || "text",
            dataValueField: ctrl.value || "code"
        };
    }
})
.directive('kendoGridRowDblClick',  
        [function kendoGridRowDblClick(){
    return{
        link: function (scope, element, attrs) {
            scope.$on("kendoWidgetCreated", function (event, widget) {

                if (widget !== element.getKendoGrid())
                    return;

                attachDblClickToRows(scope, element, attrs);

                element.data("kendoGrid").bind('dataBound', function () {
                    attachDblClickToRows(scope, element, attrs);
                });
            });
        }
    };

    function attachDblClickToRows(scope, element, attrs) {
            element.find('tbody tr').on('dblclick', function (event) {
                var rowScope = angular.element(event.currentTarget).scope();
                scope.$eval(attrs.kendoGridRowDblClick, { rowData: rowScope.dataItem });
            });
    }
}
])
.directive('comGrid',[function(){
    return {
        restrict : 'A',
        controller: ['$element', '$scope', function(element){
            var ctrl = this;
            ctrl.options = {
                            selectable: true,
                            sortable: true,
                            dataBound: function() {
                                this.select("tr:eq(0)");
                            },
                            height: 453
                        };
        }],
        controllerAs : 'comgrid',
        link: function(scope, element, attrs, ctrl){ 
            
        }
    };
}])
.component('grid',{
    template: ['<div id="grid" kendo-grid k-options="$ctrl.gridOptions"></div>'].join(''),
    bindings:{
      field : '=',
      title : '='
    },
    controller: function(){
        var ctrl = this;
        ctrl.gridOptions ={
                           toolbar:["excel","create"],
                           excel:{
                               fileName : "text.xlsx"
                           },
                           editable: {
                               createAt: "bottom"
                           },
                           dataSource : {
                               pageSize: 5,
                               transport : {
                                   read : function(options){
                                       var localData = JSON.parse(localStorage["grid_data"]);
                                       options.success(localData);
                                       },
                                   update : {
                                       
                                   },
                                   destroy : {
                                       
                                   },
                                   create : {
                                       
                                   }
                                   },
                           pageSize : 10},
                           height: 453,
                           editable: true,
                           selectable: "row",
                           sortable: true,
                           pageable: true,
                           groupable: false,
                           resizable: true,
                           reorderable: false,
                           columnMenu: true
                           };                                           
        }
})
.directive('imsCalendar',[function(){
    return {
        restrict : 'A',
        controller: ['$element', function(element){
            var ctrl = this;
            ctrl.options = {
                       start : "date",
                       depth : "year",
                       format : "yyyy-MM-dd",
                       value : new Date(),
                       change : function() {
                           var value = this.value();
                           console.log(value);
                       }};
            
        }],
        controllerAs : 'imsCalendar'
    };
}])
.component('calendar',{
    template: ['<input kendo-date-picker  FrontDate="$ctrl.front" k-options="$ctrl.frontcalendarOptions" mg-model="startDate" k-rebind="endDate"/>~<input kendo-date-picker BackDate="$crtl.Back" k-options="$ctrl.backcalendarOptions" mg-model="endDate" k-rebind="startDate"/>'].join(''),
    Scope: true,
    bindings:{
        FrontDate: '=',
        BackDate : '='
      
    },
    controller: function(){
        var ctrl = this;
        var endDate = new Date();   
        var startDate = new Date();
        ctrl.frontcalendarOptions = {
                         start : "date",
                         depth : "year",
                         format : "yyyy-MM-dd",
                         value: new Date(),
                         change: function() {
                             var value = this.value();
                             console.log(value);
                             },
                         max : endDate
                         };
        ctrl.backcalendarOptions = {
                        start : "date",
                        depth : "year",
                        format : "yyyy-MM-dd",
                        value : new Date(),
                        change : function() {
                            var value = this.value();
                            console.log(value);
                            },
                        min : startDate
                        };
    }


});