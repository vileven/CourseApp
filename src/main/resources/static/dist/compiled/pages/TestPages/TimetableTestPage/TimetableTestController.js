"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var TimetableTestController = (function () {
    function TimetableTestController() {
    }
    TimetableTestController.prototype.onCreate = function () {
        var _this = this;
        var trigger = function () {
            var element = _this.view.DOMRoot.querySelector("#timetable");
            var timetable = element.component;
            timetable.loading = !timetable.loading;
            window.setTimeout(trigger, 1000 + Math.random() * 4000);
        };
        // window.setTimeout(trigger, 1000);
        window.setTimeout(function () {
            var element = _this.view.DOMRoot.querySelector("#table");
            var table = element.component;
            var query = function () {
                var data = [];
                for (var i = 0; i < 20; i++) {
                    var names = ['Lol', 'Kek', 'Cheburek', 'Max Kekker', 'Adolf Hitlerovich'];
                    var professors = ['Mr. Katz', 'prof. Galina Ivanovna'];
                    data.push({
                        id: i + 1,
                        name: names[Math.floor(Math.random() * names.length)],
                        key: Math.floor(Math.random() * 4000),
                        professor: professors[Math.floor(Math.random() * professors.length)],
                    });
                }
                table.data.setValues(data);
                table.columns.update();
            };
            table.controller.onQuery = query;
            query();
            table.columns.setValues([
                {
                    name: 'id',
                    title: 'КОД',
                    width: 0.1,
                    order: 'asc',
                    canOrder: true,
                    isTitle: true,
                },
                {
                    name: 'name',
                    title: 'ИМЯ',
                    canOrder: true,
                    canEdit: true,
                    canFilter: true,
                },
                {
                    name: 'key',
                    title: 'КЛЮЧ',
                    width: 0.1,
                    canOrder: true,
                    foreignKey: {
                        route: 'TimetableTestRoute',
                        valueKey: 'key',
                    }
                },
                {
                    name: 'visit',
                    title: 'КНОПКА',
                    width: 0.1,
                    type: 'button',
                    buttonText: 'Выкинуть',
                    onButtonClick: function (table, data) { alert(data.name + " \u0432\u044B\u043A\u0438\u043D\u0443\u0442!"); },
                },
                {
                    name: 'professor',
                    title: 'ПРЕПОДАВАТЕЛЬ',
                    canOrder: true,
                    canEdit: true,
                    canFilter: true,
                    type: 'select',
                    selectList: [
                        'Mr. Catz',
                        'prof. Galina Ivanova',
                    ],
                }
            ]);
        }, 100);
    };
    return TimetableTestController;
}());
TimetableTestController = __decorate([
    JSWorks.Controller
], TimetableTestController);
exports.TimetableTestController = TimetableTestController;
//# sourceMappingURL=TimetableTestController.js.map