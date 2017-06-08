"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var CurrentUserHelper_1 = require("../helpers/CurrentUserHelper");
var CurrentUserInterceptor = (function () {
    function CurrentUserInterceptor() {
    }
    CurrentUserInterceptor.prototype.intercept = function (args) {
        return new Promise(function (resolve, reject) {
            CurrentUserHelper_1.CurrentUserHelper.currentUser.then(function (user) {
                if (!args['nextPage']) {
                    return;
                }
                JSWorks.applicationContext.currentPage['currentUser'] = user;
                resolve();
            }).catch(function (err) {
                resolve();
            });
        });
    };
    return CurrentUserInterceptor;
}());
CurrentUserInterceptor = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.RouteAfterNavigateInterceptor })
], CurrentUserInterceptor);
exports.CurrentUserInterceptor = CurrentUserInterceptor;
//# sourceMappingURL=CurrentUserInterceptor.js.map