"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var FormBeforeSubmitInterceptor = (function () {
    function FormBeforeSubmitInterceptor() {
    }
    FormBeforeSubmitInterceptor.prototype.intercept = function (args) {
        JSWorks.applicationContext.currentPage.loading = true;
        JSWorks.applicationContext.currentPage.error = undefined;
        return Promise.resolve();
    };
    return FormBeforeSubmitInterceptor;
}());
FormBeforeSubmitInterceptor = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.FormBeforeSubmitInterceptor })
], FormBeforeSubmitInterceptor);
exports.FormBeforeSubmitInterceptor = FormBeforeSubmitInterceptor;
var FormAfterSubmitInterceptor = (function () {
    function FormAfterSubmitInterceptor() {
    }
    FormAfterSubmitInterceptor.prototype.intercept = function (args) {
        JSWorks.applicationContext.currentPage.loading = false;
        if (!args['success']) {
            JSWorks.applicationContext.currentPage.error = args['result'];
        }
        if (args['result'] !== undefined) {
            args['form'].clear();
        }
        return Promise.resolve();
    };
    return FormAfterSubmitInterceptor;
}());
FormAfterSubmitInterceptor = __decorate([
    JSWorks.Interceptor({ type: JSWorks.InterceptorType.FormAfterSubmitInterceptor })
], FormAfterSubmitInterceptor);
exports.FormAfterSubmitInterceptor = FormAfterSubmitInterceptor;
//# sourceMappingURL=FormLoadingInterceptors.js.map