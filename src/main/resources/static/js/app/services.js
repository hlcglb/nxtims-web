/*
 * AngularJS Service Module 설정 js
 *
 * @author 정철균
 */

'use strict';

/**
 * auth Module
 */
angular.module('service.kendo.data',[])
.factory('KendoDataHelper',[function(){
    
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
     * @Param Array
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
    /**
     * type에 따른 kendo datasource 리턴
     */
    var toKendoData = function(data, type){
        
        var dataSource = null;
        switch(type) {
        case 'tree':
            var treelist = transformHierarchical(data);
            dataSource = new kendo.data.HierarchicalDataSource(
                    { data: treelist});
            if(dataSource.data().length == 0){
                dataSource.read();
            }
            
            break;
        case 'grid':
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
