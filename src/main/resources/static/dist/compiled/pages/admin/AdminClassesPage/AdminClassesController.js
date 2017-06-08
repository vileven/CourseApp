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
var AdminClassesController = (function (_super) {
    __extends(AdminClassesController, _super);
    function AdminClassesController() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.modelName = 'ClassModel';
        _this.addFormName = 'ClassAddFormView';
        return _this;
    }
    AdminClassesController.prototype.onFormOpen = function (form) {
        var calendar = form
            .querySelector('.select-date').component;
        var dateInput = form.querySelector('.show-calendar');
        dateInput.removeEventListeners('focus');
        dateInput.addEventListener('focus', function () {
            var boundingRect = dateInput.rendered.getBoundingClientRect();
            calendar.controller.setDate(dateInput.rendered.value);
            calendar.controller.show(boundingRect.left, boundingRect.top);
        });
        dateInput.removeEventListeners('blur');
        dateInput.addEventListener('blur', function () {
            /* window.setTimeout(() => {
                calendar.controller.hide();
            }, 100); */
        });
        calendar.controller.onSelect = function (calendar, date) {
            dateInput.rendered['value'] = date.toDateString();
        };
        var virtualDOM = JSWorks.applicationContext.serviceHolder
            .getServiceByName('SimpleVirtualDOM');
        var select = form.querySelector('.select-course');
        if (!select['_adminPatched']) {
            select.addEventListener('change', function () {
                var value = select.rendered.value;
                if (!value.includes('-')) {
                    return;
                }
                var courseId = parseInt(String(value).split('-')[0].trim(), 10);
                JSWorks.applicationContext.modelHolder.getModel('CourseModel')
                    .groupsAndSubjects(courseId)
                    .then(function (groupsAndSubjects) {
                    var selectGroup = form.querySelector('.select-group');
                    selectGroup.removeChildren();
                    var option = virtualDOM.createElement('OPTION');
                    option.appendChild(virtualDOM.createTextElement('Выберите группы:'));
                    option.setAttribute('selected', 'selected');
                    selectGroup.appendChild(option);
                    groupsAndSubjects.groups.forEach(function (group) {
                        var option = virtualDOM.createElement('OPTION');
                        option.appendChild(virtualDOM.createTextElement(group.id + " - " + group.name));
                        selectGroup.appendChild(option);
                    });
                    var selectSubject = form.querySelector('.select-subject');
                    selectSubject.removeChildren();
                    option = virtualDOM.createElement('OPTION');
                    option.appendChild(virtualDOM.createTextElement('Выберите предмет:'));
                    option.setAttribute('selected', 'selected');
                    selectSubject.appendChild(option);
                    groupsAndSubjects.subjects.forEach(function (subject) {
                        var option = virtualDOM.createElement('OPTION');
                        option.appendChild(virtualDOM.createTextElement(subject.id + " - " + subject.name));
                        selectSubject.appendChild(option);
                    });
                });
            });
        }
        return JSWorks.applicationContext.modelHolder.getModel('CourseModel')
            .query({ limit: 1000, offset: 0, orders: [["name", "ASC"]], filters: [] })
            .then(function (courses) {
            var selectCourse = form.querySelector('.select-course');
            selectCourse.removeChildren();
            var option = virtualDOM.createElement('OPTION');
            option.appendChild(virtualDOM.createTextElement('Выберите курс:'));
            option.setAttribute('selected', 'selected');
            selectCourse.appendChild(option);
            courses.forEach(function (course) {
                var option = virtualDOM.createElement('OPTION');
                option.appendChild(virtualDOM.createTextElement(course.id + " - " + course.name));
                selectCourse.appendChild(option);
            });
        });
    };
    AdminClassesController.prototype.setup = function () {
        this.table.title = 'Занятия';
        this.table.columns.setValues([
            {
                name: 'id',
                title: 'ID',
                width: 0.1,
                canOrder: true,
            },
            {
                name: '_date',
                title: 'ДАТА',
            },
            {
                name: '_times',
                title: 'ВРЕМЯ',
            },
            {
                name: 'subject_name',
                title: 'ПРЕДМЕТ',
            },
            {
                name: 'group_name',
                title: 'КУРС',
            },
        ]);
    };
    return AdminClassesController;
}(AbstractAdminPageController_1.AbstractAdminPageController));
AdminClassesController = __decorate([
    JSWorks.Controller
], AdminClassesController);
exports.AdminClassesController = AdminClassesController;
//# sourceMappingURL=AdminClassesController.js.map