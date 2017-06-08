"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var CourseValidator = (function () {
    function CourseValidator() {
    }
    CourseValidator.prototype.intercept = function (args) {
        return new Promise(function (resolve, reject) {
            if (args['value'].includes(' - ')) {
                resolve();
                return;
            }
            reject('Выберите курс');
        });
    };
    return CourseValidator;
}());
CourseValidator = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.ValidatorInterceptor })
], CourseValidator);
exports.CourseValidator = CourseValidator;
//# sourceMappingURL=CourseValidator.js.map