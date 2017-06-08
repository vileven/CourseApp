"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var SignUpPage = (function () {
    function SignUpPage() {
        this.loading = false;
    }
    return SignUpPage;
}());
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Boolean)
], SignUpPage.prototype, "loading", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", String)
], SignUpPage.prototype, "error", void 0);
SignUpPage = __decorate([
    JSWorks.Page({ view: 'SignUpView', controller: 'SignUpController' })
], SignUpPage);
exports.SignUpPage = SignUpPage;
//# sourceMappingURL=SignUpPage.js.map