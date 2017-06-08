"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var SignUpController = (function () {
    function SignUpController() {
    }
    SignUpController.prototype.onCreate = function () {
        var _this = this;
        this.form = this.view.DOMRoot.querySelector('#SignUpForm');
        this.form.onSuccess = function (form, data) {
            _this.form.clear();
            window.setTimeout(function () {
                JSWorks.applicationContext.router.navigate(JSWorks.applicationContext.routeHolder.getRoute('LoginRoute'), (_a = {}, _a[':email'] = data['email'], _a));
                var _a;
            }, 10);
            return true;
        };
    };
    SignUpController.prototype.onNavigate = function (args) {
        this.onCreate();
    };
    return SignUpController;
}());
SignUpController = __decorate([
    JSWorks.Controller
], SignUpController);
exports.SignUpController = SignUpController;
//# sourceMappingURL=SignUpController.js.map