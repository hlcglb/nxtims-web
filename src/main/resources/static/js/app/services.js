/*
 * AngularJS Service Module 설정 js
 *
 * @author 정철균
 */

'use strict';

/**
 * auth Module
 */


angular.module('nxtims.services',[])
/*
 * kendo datasource로 변환 
 */
.factory('KendoDataHelper',['UserService', function(UserService){
    
    var transform = function(object, handler) {
        if(jQuery.isPlainObject(object) || angular.isArray(object)) {
            for( var key in object) {
                var value = object[key];
                if(angular.isFunction(handler)) {
                    handler(object, key, value);
                }
                transform(value, handler);
            }
        }
    };
    
    /**
     * Json -> kendo tree 구조로 변경
     * RESOURCE_LEVEL로 단계 구분(1부터 최상위 노드) 후 정렬된 데이터만 변경 가능
     * @Param {Array}
     */
    var transformHierarchical = function(arr) {
        var map = {}, node, roots = [];
        for(var i=0; i < arr.length; i++){
            node = arr[i];
            map[node.RESOURCE_LEVEL] = i;
            var nextkey = (i + 1) > (arr.length - 1) ? i : i + 1;
            var prevkey = (i - 1) < 0 ? i : i -1;
            node["text"] = node.RESOURCE_NM;
            if(node.RESOURCE_LEVEL == "1" || Number(node.RESOURCE_LEVEL) < Number(arr[nextkey].RESOURCE_LEVEL)){
                node["items"] = [];
            }
            // parent id == -1 은 최상위
            node["PARENT_ID"] = angular.isUndefined(map[String(Number(node.RESOURCE_LEVEL)-1)]) ? 
                    "-1" : arr[map[String(Number(node.RESOURCE_LEVEL)-1)]].RESOURCE_ID;

            if (node["PARENT_ID"] != "-1" ) {
                arr[map[Number(node.RESOURCE_LEVEL-1)]].items.push(node);
            } else {
                roots.push(node);
            }
        }
        return roots;
    };
    
    var isEmpty = function(object) {
        if(object == null || object == "") {
            return true;
        }
        return false;
    }
    
    var setUdateData = function(dataSource, orgModel){
        var model = orgModel.toJSON();
        model.uid = angular.copy(orgModel.uid);
        var updateData = dataSource.UPDATE;
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
                }
                if(idx > -1){
                    updateData[i].TRANSACTION_TYPE == "C" ? updateData.splice(i,1) : updateData[i] = model;
                }
                else updateData.push(model);
                break;
            }
        }
        else{
            dataSource.UPDATE = new Array();
            if(model.TRANSACTION_TYPE == "D"){
                for(var i=0; i<dataSource._destroyed.length; i++){
                    dataSource.UPDATE.push(dataSource._destroyed[i]);
                }
            }
            else dataSource.UPDATE.push(model);
        }
        console.log(dataSource);
    };
    /**
     * type에 따른 kendo datasource 리턴
     * 
     */
    var toKendoData = function(data, type){
        
        var dataSource = null;
        switch(type) {
        case 'tree':
            dataSource = new kendo.data.HierarchicalDataSource(
                    { data: data});
            if(dataSource.data().length == 0){
                dataSource.read();
            }
            
            break;
        case 'data':
            var defualtDataSource = {
                data: [],
                schema: {
                    model: {}
                },
                pageSize: 10
            };
            var changeFunction = function(e){
                switch(e.action){
                case "add" :
                    e.items[0].TRANSACTION_TYPE = "C";
                case "itemchange" :
                    console.log("itemchange --------");console.log(e);
                    var model = e.items[0];
                    if(model.dirty){
                        (model.isNew()) ? model.TRANSACTION_TYPE = "C" : model.TRANSACTION_TYPE = "U";
                        if(model.SESSION_USER_ID == null) model.SESSION_USER_ID = UserService.getUser().USER_ID;
                    }
                    setUdateData(this, model);
                    if(angular.isFunction(data.change)){
                        data.change(e);
                    }
                    break;
                case "remove" :
                    console.log("remove --------");console.log(e);
                    var model = e.items[0];
                    model.TRANSACTION_TYPE = "D";
                    setUdateData(this, model);
                    if(angular.isFunction(data.change)){
                        data.change(e);
                    }
                    break;
                case "sync":
                    console.log("sync --------");console.log(e);
                    break;
                default:
                    if(this.UPDATE) delete this.UPDATE;
                    break;
                }
                
            };
            angular.extend(defualtDataSource, data);
            defualtDataSource.change = changeFunction;
            dataSource = new kendo.data.DataSource(defualtDataSource);
            break;
        default:
            break;
        }
        return dataSource;
    };
    
    return {
        toKendoData: toKendoData
    };
    
    

}]);
