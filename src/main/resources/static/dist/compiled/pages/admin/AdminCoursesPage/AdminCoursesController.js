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
var AdminCoursesController = (function (_super) {
    __extends(AdminCoursesController, _super);
    function AdminCoursesController() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.modelName = 'CourseModel';
        _this.addFormName = 'CourseAddFormView';
        return _this;
    }
    AdminCoursesController.prototype.setup = function () {
        this.table.title = 'Курсы';
        this.table.columns.setValues([
            {
                name: 'id',
                title: 'ID',
                width: 0.1,
                canOrder: true,
            },
            {
                name: 'name',
                title: 'НАЗВАНИЕ',
                canOrder: true,
                canEdit: true,
                canFilter: true,
            }
        ]);
    };
    return AdminCoursesController;
}(AbstractAdminPageController_1.AbstractAdminPageController));
AdminCoursesController = __decorate([
    JSWorks.Controller
], AdminCoursesController);
exports.AdminCoursesController = AdminCoursesController;
//# sourceMappingURL=AdminCoursesController.js.map