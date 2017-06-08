"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var firstPassword;
var PasswordsMatch1Validator = (function () {
    function PasswordsMatch1Validator() {
    }
    PasswordsMatch1Validator.prototype.intercept = function (args) {
        return new Promise(function (resolve, reject) {
            firstPassword = args['value'];
            resolve();
        });
    };
    return PasswordsMatch1Validator;
}());
PasswordsMatch1Validator = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.ValidatorInterceptor })
], PasswordsMatch1Validator);
exports.PasswordsMatch1Validator = PasswordsMatch1Validator;
var PasswordsMatch2Validator = (function () {
    function PasswordsMatch2Validator() {
    }
    PasswordsMatch2Validator.prototype.intercept = function (args) {
        return new Promise(function (resolve, reject) {
            if (firstPassword === args['value']) {
                resolve();
                return;
            }
            reject('Пароли не совпадают!');
        });
    };
    return PasswordsMatch2Validator;
}());
PasswordsMatch2Validator = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.ValidatorInterceptor })
], PasswordsMatch2Validator);
exports.PasswordsMatch2Validator = PasswordsMatch2Validator;
//# sourceMappingURL=PasswordsMatchValidator.js.map