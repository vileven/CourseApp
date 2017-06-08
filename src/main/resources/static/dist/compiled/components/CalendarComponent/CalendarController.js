"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var CalendarController = CalendarController_1 = (function () {
    function CalendarController() {
        this.visible = false;
    }
    CalendarController.prototype.onCreate = function () {
    };
    CalendarController.toRussianWeekday = function (americanWeekday) {
        americanWeekday -= 1;
        if (americanWeekday < 0) {
            return 6;
        }
        return americanWeekday;
    };
    CalendarController.prototype.setDate = function (date) {
        var months = [
            'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь',
            'Октябрь', 'Ноябрь', 'Декабрь',
        ];
        if (date === '') {
            date = new Date();
        }
        date = new Date(date);
        this.component.monthName = months[date.getMonth()].toUpperCase() + " " + date.getFullYear();
        this.component.date = date;
        this.updateCalendar();
    };
    CalendarController.prototype.updateCalendar = function () {
        var _this = this;
        var weekdayNames = [
            'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс',
        ];
        var table = this.view.DOMRoot.querySelector('.calendar-table').component;
        table.paginator = false;
        table.toolbox = false;
        table.selectable = false;
        table.onCellClick = function (table, row, column, data) {
            if (!_this.visible) {
                return;
            }
            var date = new Date(_this.component.date.getFullYear(), _this.component.date.getMonth(), data[weekdayNames[column]]);
            _this.setDate(date);
            _this.hide();
            if (_this.onSelect) {
                _this.onSelect(_this.component, date);
            }
        };
        var back = this.view.DOMRoot.querySelector('.calendar-month-left');
        back.removeEventListeners('click');
        back.addEventListener('click', function () {
            var newMonth = _this.component.date.getMonth() - 1;
            var newYear = _this.component.date.getFullYear();
            if (newMonth < 0) {
                newMonth = 11;
                newYear -= 1;
            }
            _this.setDate(new Date(newYear, newMonth, 1));
        });
        var forward = this.view.DOMRoot.querySelector('.calendar-month-right');
        forward.removeEventListeners('click');
        forward.addEventListener('click', function () {
            var newMonth = _this.component.date.getMonth() + 1;
            var newYear = _this.component.date.getFullYear();
            if (newMonth > 11) {
                newMonth = 0;
                newYear += 1;
            }
            _this.setDate(new Date(newYear, newMonth, 1));
        });
        var weeks = [];
        var weekdays = {};
        var weekdaysCount = 0;
        var month = this.component.date.getMonth();
        var startDate = new Date(this.component.date.getFullYear(), month, 1);
        for (var date = startDate; date.getMonth() === month; date.setDate(date.getDate() + 1)) {
            var weekday = CalendarController_1.toRussianWeekday(date.getDay());
            if (weekdaysCount === 0 && weekday > 0) {
                for (var i = 0; i < weekday; i++) {
                    weekdays[weekdayNames[weekday]] = '';
                    weekdaysCount++;
                }
            }
            weekdays[weekdayNames[weekday]] = "" + date.getDate();
            weekdaysCount++;
            var daysInMonth = new Date(date.getFullYear(), date.getMonth(), 0).getDate();
            if (weekdaysCount > 6) {
                weeks.push(weekdays);
                weekdays = {};
                weekdaysCount = 0;
            }
        }
        if (weekdaysCount !== 0) {
            weeks.push(weekdays);
        }
        table.data.setValues(weeks);
        table.columns.setValues(weekdayNames.map(function (dayName) {
            return {
                name: dayName,
                title: dayName,
            };
        }));
    };
    CalendarController.prototype.show = function (left, top) {
        var element = this.view.DOMRoot.querySelector('.calendar-container');
        this.visible = true;
        element.setStyleAttribute('left', Math.round(left) + "px");
        element.setStyleAttribute('top', Math.round(top) + "px");
        element.toggleClass('hidden', false);
    };
    CalendarController.prototype.hide = function () {
        var element = this.view.DOMRoot.querySelector('.calendar-container');
        element.toggleClass('hidden', true);
        this.visible = false;
    };
    CalendarController.prototype.onDOMInsert = function () {
        this.setDate(new Date());
    };
    return CalendarController;
}());
CalendarController = CalendarController_1 = __decorate([
    JSWorks.Controller
], CalendarController);
exports.CalendarController = CalendarController;
var CalendarController_1;
//# sourceMappingURL=CalendarController.js.map