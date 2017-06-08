"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var AbstractAdminPageController_1 = require("../AbstractAdminPageController");
var AdminUsersController = (function (_super) {
    __extends(AdminUsersController, _super);
    function AdminUsersController() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.modelName = 'UserModel';
        _this.addFormName = 'UserAddFormView';
        return _this;
    }
    AdminUsersController.prototype.setup = function () {
        this.table.title = 'Пользователи';
        this.table.columns.setValues([
            {
                name: 'id',
                title: 'ID',
                width: 0.1,
                canOrder: true,
            },
            {
                name: 'first_name',
                title: 'ИМЯ',
                canOrder: true,
                canEdit: true,
                canFilter: true,
            },
            {
                name: 'last_name',
                title: 'ФАМИЛИЯ',
                canOrder: true,
                canEdit: true,
                canFilter: true,
            },
            {
                name: 'email',
                title: 'EMAIL',
                canOrder: true,
                canEdit: true,
                canFilter: true,
            },
            {
                name: 'role_text',
                title: 'РОЛЬ',
                canEdit: true,
                type: 'select',
                selectList: [
                    'Студенты',
                    'Преподаватели',
                    'Администраторы',
                ],
            },
        ]);
    };
    return AdminUsersController;
}(AbstractAdminPageController_1.AbstractAdminPageController));
AdminUsersController = __decorate([
    JSWorks.Controller
], AdminUsersController);
exports.AdminUsersController = AdminUsersController;
//# sourceMappingURL=AdminUsersController.js.map