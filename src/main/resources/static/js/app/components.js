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
});