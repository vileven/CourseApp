"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var TimeValidator = (function () {
    function TimeValidator() {
    }
    TimeValidator.prototype.intercept = function (args) {
        return new Promise(function (resolve, reject) {
            if (/\d{1,2}:\d{1,2}/g.exec(String(args['value']))) {
                resolve();
                return;
            }
            reject('Неправильный формат времени! Ожидается: 00:00');
        });
    };
    return TimeValidator;
}());
TimeValidator = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.ValidatorInterceptor })
], TimeValidator);
exports.TimeValidator = TimeValidator;
//# sourceMappingURL=TimeValidator.js.map