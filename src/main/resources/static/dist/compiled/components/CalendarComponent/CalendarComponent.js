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
var CalendarComponent = (function () {
    function CalendarComponent() {
        this.monthName = 'ЯНВАРЬ 1970';
        this.date = new Date();
    }
    return CalendarComponent;
}());
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", String)
], CalendarComponent.prototype, "monthName", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Date)
], CalendarComponent.prototype, "date", void 0);
CalendarComponent = __decorate([
    JSWorks.Component({ view: 'CalendarView', controller: 'CalendarController' })
], CalendarComponent);
exports.CalendarComponent = CalendarComponent;
//# sourceMappingURL=CalendarComponent.js.map