"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var CurrentUserHelper_1 = require("../../helpers/CurrentUserHelper");
var LandingController = (function () {
    function LandingController() {
    }
    LandingController.prototype.onCreate = function () {
    };
    LandingController.prototype.onNavigate = function (args) {
        if (args[':random']) {
            CurrentUserHelper_1.CurrentUserHelper.currentUser = JSWorks.applicationContext.modelHolder
                .getModel('UserModel').logout();
        }
        window.setTimeout(function () {
            JSWorks.applicationContext.router.navigate(JSWorks.applicationContext.routeHolder.getRoute('LoginRoute'), {});
        }, 10);
    };
    return LandingController;
}());
LandingController = __decorate([
    JSWorks.Controller
], LandingController);
exports.LandingController = LandingController;
//# sourceMappingURL=LandingController.js.map