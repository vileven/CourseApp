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
var GroupModel = (function (_super) {
    __extends(GroupModel, _super);
    function GroupModel() {
        var _this = _super.call(this) || this;
        _this.controllerUrl = 'group';
        return _this;
    }
    Object.defineProperty(GroupModel.prototype, "_course_id", {
        set: function (value) {
            if (typeof value === 'number') {
                this.course_id = value;
                return;
            }
            this.course_id = parseInt(String(value).split('-')[0].trim(), 10);
        },
        enumerable: true,
        configurable: true
    });
    GroupModel.prototype.query = function (params) {
        return _super.prototype.query.call(this, params);
    };
    GroupModel.prototype.create = function () {
        return _super.prototype.create.call(this);
    };
    GroupModel.prototype.read = function (id) {
        return _super.prototype.read.call(this, id);
    };
    GroupModel.prototype[_a = 'delete'] = function () {
        return _super.prototype.delete.call(this);
    };
    GroupModel.prototype.update = function () {
        return _super.prototype.update.call(this);
    };
    return GroupModel;
}(AbstractModel_1.AbstractModel));
__decorate([
    JSWorks.ModelField,
    JSWorks.ModelPrimaryKey,
    __metadata("design:type", Number)
], GroupModel.prototype, "id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", Number)
], GroupModel.prototype, "course_id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], GroupModel.prototype, "name", void 0);
__decorate([
    JSWorks.ModelQueryMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], GroupModel.prototype, "query", null);
__decorate([
    JSWorks.ModelCreateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], GroupModel.prototype, "create", null);
__decorate([
    JSWorks.ModelReadMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number]),
    __metadata("design:returntype", Promise)
], GroupModel.prototype, "read", null);
__decorate([
    JSWorks.ModelDeleteMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], GroupModel.prototype, _a, null);
__decorate([
    JSWorks.ModelUpdateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], GroupModel.prototype, "update", null);
GroupModel = __decorate([
    JSWorks.Model,
    __metadata("design:paramtypes", [])
], GroupModel);
exports.GroupModel = GroupModel;
var _a;
//# sourceMappingURL=GroupModel.js.map