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
var TimetableComponent = (function () {
    function TimetableComponent() {
        this._classes = [];
        this.loading = false;
        this.error = false;
        this.timetable = [
            {
                title: 'Caturday, 25 мая',
                classes: [
                    {
                        title: 'Memology',
                        professor: 'Mr. Katz',
                        location: '806',
                        beginTime: new Date('2017-05-15T16:30:00'),
                        endTime: new Date('2017-05-15T18:00:00'),
                    },
                    {
                        title: 'МЗЯиОК',
                        professor: 'prof. Galina Ivanova',
                        location: '806ы',
                        beginTime: new Date('2017-05-15T18:15:00'),
                        endTime: new Date('2017-05-15T20:00:00'),
                    }
                ]
            },
            {
                title: 'Sunday, 26 мая',
                classes: [
                    {
                        title: 'МЗЯиОК',
                        professor: 'prof. Galina Ivanova',
                        location: '806ы',
                        beginTime: new Date('2017-05-15T18:15:00'),
                        endTime: new Date('2017-05-15T20:00:00'),
                    }
                ]
            }
        ];
    }
    TimetableComponent.prototype.formatTime = function (beginTime, endTime) {
        var format = function (time) {
            return time.getHours() + ":" + ((time.getMinutes() < 10) ? ('0' + time.getMinutes()) : (time.getMinutes()));
        };
        return format(beginTime) + " \u2014 " + format(endTime);
    };
    return TimetableComponent;
}());
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Boolean)
], TimetableComponent.prototype, "loading", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Boolean)
], TimetableComponent.prototype, "error", void 0);
__decorate([
    JSWorks.ComponentCollectionProperty(),
    __metadata("design:type", Array)
], TimetableComponent.prototype, "timetable", void 0);
TimetableComponent = __decorate([
    JSWorks.Component({ view: 'TimetableView', controller: 'TimetableController' })
], TimetableComponent);
exports.TimetableComponent = TimetableComponent;
//# sourceMappingURL=TimetableComponent.js.map