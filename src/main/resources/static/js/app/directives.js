/*
 * AngularJS Directive 설정 js
 *
 * @author 정철균
 */

angular.module('directives.kendo.menu',[])
.directive('programMenuTree', ['$window', function ($window) {
    return {
        scope: true,
        template: ['<div kendo-tree-view="menu.tree" k-options="menu.treeSetting" k-data-source="menu.data" k-ng-delay ="menu.data" ng-click="menu.toggle($event)">',
                   '</div>'].join(''),
        //templateUrl: "./js/templates/components/mainlayout/menu.html",
        controller: ['$scope', '$timeout', '$window', function($scope, $timeout, $window){
            //var model = $parse(attrs.attr2);
            var menu = this;
            menu.treeSetting = {
                                
            };
            $timeout(function(){
                console.log(123);
                menu.data = $scope.$parent.controller.treelist;
            });
            
        }],
        controllerAs: 'menu',
        link: function($scope, element, attrs, ctrl) {
            //ctrl == $scope.menu
            //ctrl != this
            
            ctrl.toggle = function(e){
                //e.preventDefault();
                var target = angular.element(e.target);
                var toggleIcon = target.closest(".k-icon");
                // ctrl == this
                if (!toggleIcon.length) {
                    var currData = ctrl.tree.dataItem(ctrl.tree.select());
                    console.log(currData);
                    if(angular.equals(currData.RESOURCE_URL, null)){
                        this.tree.toggle(target.closest(".k-item"));
                    }
                    else{
                        //$scope.tabAdd.add({programId: currData.value});
                        $window.open(currData.RESOURCE_URL);
                    }
                    
                }
            };
        }
    };

}])
.directive('programPage1', ['$window', '$compile', function ($window, $compile) {
    return {
        restrict: 'EA',
        controller: ['$scope', '$element', function($scope, $element){
            $scope.window = $window.open('', '_blank');
            angular.element($scope.window.document.body).append($compile($element.content())($scope));
        }],
        link: function($scope, element, attrs, ctrl) {
            element.on('$destroy', function(){
                $scope.window.close();
            })
        }
    };

}])
.directive('nHref', ['$window', '$compile', function ($window, $compile) {
    return {
        restrict: 'A',
        compile: [function(element, attrs){
            console.log(attrs);
            element.attr('target', '_blank');
        }]
    };

}]);