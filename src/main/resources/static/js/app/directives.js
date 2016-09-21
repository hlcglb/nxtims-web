/*
 * AngularJS Directive 설정 js
 *
 * @author 정철균
 */
'use strict';

angular.module('nxtims.directives',[]);
angular.module('nxtims.directives')
.directive('imsGrid', [function() {
    function setUdateData(grid, orgModel){
        var model = orgModel.toJSON();
        model.uid = angular.copy(orgModel.uid);
        var updateData = grid.dataSource.UPDATE;
        var idx = -1;
        if(updateData){
            switch(model.TRANSACTION_TYPE){
            case "C":
            case "U":
                for(var i in updateData){
                    if(updateData[i].uid == model.uid) idx = i;
                }
                idx > -1 ? updateData[idx] = model : updateData.push(model);
                break;
            case "D":
                for(var i in updateData){
                    if(updateData[i].uid == model.uid) idx = i;
                    if(updateData[i].uid == model.uid){
                        console.log(updateData[i].TRANSACTION_TYPE);
                    }
                }
                if(idx > -1){
                    updateData[i].TRANSACTION_TYPE == "C" ? updateData.splice(i,1) : updateData[i] = model;
                }
                else updateData.push(model);
                break;
            }
        }
        else{
            grid.dataSource.UPDATE = [model];
        }
        
        console.log(grid.dataSource);
    };
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
                                //this.expandRow(this.tbody.find("tr.k-master-row").first());
                                this.select("tr:eq(0)");
                                console.log("data bound");
                                if(this.dataSource.UPDATE) delete this.dataSource.UPDATE;
                            },
                            save: function(e){
                                console.log(e);
                                if(e.model.dirty){
                                    if(e.model.isNew()) e.model.TRANSACTION_TYPE = "C";
                                    else e.model.TRANSACTION_TYPE = "U";
                                    setUdateData(this, e.model);
                                }
                                
                            },
                            saveChanges: function(e){
                                console.log("save change");
                                console.log(e);
                            },
                            remove: function(e){
                                console.log("remove");
                                e.model.TRANSACTION_TYPE = "D";
                                setUdateData(this, e.model);
                            },
                            edit: function(e){
                                console.log(e);
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