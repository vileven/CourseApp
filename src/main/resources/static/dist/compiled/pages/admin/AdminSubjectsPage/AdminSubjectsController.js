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
var AdminSubjectsController = (function (_super) {
    __extends(AdminSubjectsController, _super);
    function AdminSubjectsController() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.modelName = 'SubjectModel';
        _this.addFormName = 'SubjectAddFormView';
        return _this;
    }
    AdminSubjectsController.prototype.onFormOpen = function (form) {
        return JSWorks.applicationContext.modelHolder.getModel('CourseModel')
            .query({ limit: 1000, offset: 0, orders: [], filters: [] })
            .then(function (courses) {
            var virtualDOM = JSWorks.applicationContext.serviceHolder
                .getServiceByName('SimpleVirtualDOM');
            var select = form.querySelector('select');
            select.removeChildren();
            var option = virtualDOM.createElement('OPTION');
            option.appendChild(virtualDOM.createTextElement('Выберите курс:'));
            option.setAttribute('selected', 'selected');
            select.appendChild(option);
            courses.forEach(function (course) {
                var option = virtualDOM.createElement('OPTION');
                option.appendChild(virtualDOM.createTextElement(course.id + " - " + course.name));
                select.appendChild(option);
            });
        });
    };
    AdminSubjectsController.prototype.setup = function () {
        this.table.title = 'Предметы';
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
            },
            {
                name: 'course_name',
                title: 'НАЗВАНИЕ КУРСА',
            },
        ]);
    };
    return AdminSubjectsController;
}(AbstractAdminPageController_1.AbstractAdminPageController));
AdminSubjectsController = __decorate([
    JSWorks.Controller
], AdminSubjectsController);
exports.AdminSubjectsController = AdminSubjectsController;
//# sourceMappingURL=AdminSubjectsController.js.map