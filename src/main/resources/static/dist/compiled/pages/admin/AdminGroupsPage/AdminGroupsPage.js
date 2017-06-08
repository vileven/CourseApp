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
var UserModel_1 = require("../../../models/UserModel");
var AdminGroupsPage = (function () {
    function AdminGroupsPage() {
        this.dummy = '';
    }
    return AdminGroupsPage;
}());
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", String)
], AdminGroupsPage.prototype, "dummy", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", UserModel_1.UserModel)
], AdminGroupsPage.prototype, "currentUser", void 0);
AdminGroupsPage = __decorate([
    JSWorks.Page({ view: 'AdminGroupsView', controller: 'AdminGroupsController' })
], AdminGroupsPage);
exports.AdminGroupsPage = AdminGroupsPage;
//# sourceMappingURL=AdminGroupsPage.js.map