"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var LoadingBlockController = (function () {
    function LoadingBlockController() {
    }
    LoadingBlockController.prototype.onCreate = function () {
        var load = this.view.DOMRoot.querySelector('.balls');
        var radius = 14;
        var circlesNumber = 8;
        var wrap = 64 - 4;
        for (var i = 0; i < circlesNumber; i++) {
            var f = 2 / circlesNumber * i * Math.PI;
            var circle = JSWorks.applicationContext.serviceHolder.
                getServiceByName('SimpleVirtualDOM').createElement('SPAN');
            circle.setAttribute('class', 'circle flicker');
            circle.style['left'] = (Math.cos(f) * radius + wrap / 2) + 'px';
            circle.style['top'] = (Math.sin(f) * radius + wrap / 2) + 'px';
            load.appendChild(circle);
        }
    };
    return LoadingBlockController;
}());
LoadingBlockController = __decorate([
    JSWorks.Controller
], LoadingBlockController);
exports.LoadingBlockController = LoadingBlockController;
//# sourceMappingURL=LoadingBlockController.js.map