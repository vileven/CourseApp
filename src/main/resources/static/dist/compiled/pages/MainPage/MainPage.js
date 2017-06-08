'use strict';
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
var UserModel_1 = require("../../models/UserModel");
var MainPage = (function () {
    function MainPage() {
        this.loading = false;
    }
    return MainPage;
}());
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Boolean)
], MainPage.prototype, "loading", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", UserModel_1.UserModel)
], MainPage.prototype, "currentUser", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Object)
], MainPage.prototype, "info", void 0);
MainPage = __decorate([
    JSWorks.Page({ view: 'MainView', controller: 'MainController' })
], MainPage);
exports.MainPage = MainPage;
//# sourceMappingURL=MainPage.js.map