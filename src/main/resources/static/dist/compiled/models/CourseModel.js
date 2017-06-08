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
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var AbstractModel_1 = require("./AbstractModel");
var CourseModel = (function (_super) {
    __extends(CourseModel, _super);
    function CourseModel() {
        var _this = _super.call(this) || this;
        _this.controllerUrl = 'course';
        return _this;
    }
    CourseModel.prototype.query = function (params) {
        return _super.prototype.query.call(this, params);
    };
    CourseModel.prototype.create = function () {
        return _super.prototype.create.call(this);
    };
    CourseModel.prototype.read = function (id) {
        return _super.prototype.read.call(this, id);
    };
    CourseModel.prototype[_a = 'delete'] = function () {
        return _super.prototype.delete.call(this);
    };
    CourseModel.prototype.update = function () {
        return _super.prototype.update.call(this);
    };
    CourseModel.prototype.groupsAndSubjects = function (courseId) {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] +
                ("/course/" + courseId + "/subjectsAndGroups"), JSWorks.HTTPMethod.GET, JSON.stringify({ course_id: courseId })).then(function (data) {
                if (data['error']) {
                    reject(data['error']);
                    return;
                }
                var groupModel = JSWorks.applicationContext.modelHolder.getModel('GroupModel');
                var subjectModel = JSWorks.applicationContext.modelHolder.getModel('SubjectModel');
                resolve({
                    groups: data['groups'].map(function (i) { return groupModel.from(i); }),
                    subjects: data['subjects'].map(function (i) { return subjectModel.from(i); }),
                });
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    return CourseModel;
}(AbstractModel_1.AbstractModel));
__decorate([
    JSWorks.ModelField,
    JSWorks.ModelPrimaryKey,
    __metadata("design:type", Number)
], CourseModel.prototype, "id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], CourseModel.prototype, "name", void 0);
__decorate([
    JSWorks.ModelQueryMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], CourseModel.prototype, "query", null);
__decorate([
    JSWorks.ModelCreateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], CourseModel.prototype, "create", null);
__decorate([
    JSWorks.ModelReadMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number]),
    __metadata("design:returntype", Promise)
], CourseModel.prototype, "read", null);
__decorate([
    JSWorks.ModelDeleteMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], CourseModel.prototype, _a, null);
__decorate([
    JSWorks.ModelUpdateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], CourseModel.prototype, "update", null);
CourseModel = __decorate([
    JSWorks.Model,
    __metadata("design:paramtypes", [])
], CourseModel);
exports.CourseModel = CourseModel;
var _a;
//# sourceMappingURL=CourseModel.js.map