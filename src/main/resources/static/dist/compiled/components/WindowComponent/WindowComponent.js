"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var WindowComponent = (function () {
    function WindowComponent() {
        this.windows = [];
        this.zOffset = 10000;
    }
    WindowComponent.prototype.openWindow = function (view) {
        var window = this.view.DOMRoot.querySelector('.window-template')
            .querySelector('.window-curtain').cloneNode();
        window.querySelector('.window-body').appendChild(view.DOMRoot);
        window.setStyleAttribute('z-index', this.zOffset + this.windows.length);
        this.view.DOMRoot.querySelector('.window-root').appendChild(window);
        this.windows.push(window);
    };
    WindowComponent.prototype.closeLastWindow = function () {
        if (this.windows.length === 0) {
            return;
        }
        this.windows[this.windows.length - 1].parentNode.removeChild(this.windows.pop());
    };
    return WindowComponent;
}());
WindowComponent = __decorate([
    JSWorks.Component({ view: 'WindowView', controller: 'WindowController' })
], WindowComponent);
exports.WindowComponent = WindowComponent;
//# sourceMappingURL=WindowComponent.js.map