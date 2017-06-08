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
var ClassModel = (function (_super) {
    __extends(ClassModel, _super);
    function ClassModel() {
        var _this = _super.call(this) || this;
        _this.controllerUrl = 'class';
        return _this;
    }
    Object.defineProperty(ClassModel.prototype, "_subject_id", {
        set: function (value) {
            if (typeof value === 'number') {
                this.subject_id = value;
                return;
            }
            this.subject_id = parseInt(String(value).split('-')[0].trim(), 10);
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(ClassModel.prototype, "_group_id", {
        set: function (value) {
            if (typeof value === 'number') {
                this.subject_id = value;
                return;
            }
            this.group_id = parseInt(String(value).split('-')[0].trim(), 10);
        },
        enumerable: true,
        configurable: true
    });
    ClassModel.prototype.query = function (params) {
        return _super.prototype.query.call(this, params);
    };
    ClassModel.prototype.create = function () {
        return _super.prototype.create.call(this);
    };
    ClassModel.prototype.read = function (id) {
        return _super.prototype.read.call(this, id);
    };
    ClassModel.prototype[_a = 'delete'] = function () {
        return _super.prototype.delete.call(this);
    };
    ClassModel.prototype.update = function () {
        return _super.prototype.update.call(this);
    };
    return ClassModel;
}(AbstractModel_1.AbstractModel));
__decorate([
    JSWorks.ModelField,
    JSWorks.ModelPrimaryKey,
    __metadata("design:type", Number)
], ClassModel.prototype, "id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "begin_time", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "end_time", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", Number)
], ClassModel.prototype, "subject_id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "subject_name", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", Number)
], ClassModel.prototype, "group_id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "group_name", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", Number)
], ClassModel.prototype, "professor_id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "professor_first_name", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "professor_last_name", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "topic", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], ClassModel.prototype, "location", void 0);
__decorate([
    JSWorks.ModelQueryMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], ClassModel.prototype, "query", null);
__decorate([
    JSWorks.ModelCreateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], ClassModel.prototype, "create", null);
__decorate([
    JSWorks.ModelReadMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number]),
    __metadata("design:returntype", Promise)
], ClassModel.prototype, "read", null);
__decorate([
    JSWorks.ModelDeleteMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], ClassModel.prototype, _a, null);
__decorate([
    JSWorks.ModelUpdateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], ClassModel.prototype, "update", null);
ClassModel = __decorate([
    JSWorks.Model,
    __metadata("design:paramtypes", [])
], ClassModel);
exports.ClassModel = ClassModel;
var _a;
//# sourceMappingURL=ClassModel.js.map