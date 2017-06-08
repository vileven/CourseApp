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
var UserModel = (function (_super) {
    __extends(UserModel, _super);
    // public getInfo(): Promise<any> {
    //     if (!this.id) {
    //         return reject();
    //     }
    //     return new Promise<any>((res, rej) => {
    //
    //     })
    // }
    function UserModel() {
        var _this = _super.call(this) || this;
        _this.controllerUrl = 'user';
        return _this;
    }
    Object.defineProperty(UserModel.prototype, "about", {
        get: function () { return ''; },
        set: function (value) { },
        enumerable: true,
        configurable: true
    });
    ;
    Object.defineProperty(UserModel.prototype, "role_text", {
        get: function () {
            switch (this.role) {
                case 0: return 'Администраторы';
                case 1: return 'Студенты';
                case 2: return 'Преподаватели';
            }
        },
        set: function (value) {
            switch (value) {
                case 'Администраторы':
                    this.role = 0;
                    return;
                case 'Студенты':
                    this.role = 1;
                    return;
                case 'Преподаватели':
                    this.role = 2;
                    return;
            }
        },
        enumerable: true,
        configurable: true
    });
    UserModel.prototype.current = function () {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] + '/session/current', JSWorks.HTTPMethod.GET).then(function (data) {
                if (!data['status']) {
                    _this.apply(data);
                }
                resolve(_this);
            }).catch(function (err) {
                resolve(_this);
            });
        });
    };
    UserModel.prototype.login = function () {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] + '/session/login', JSWorks.HTTPMethod.POST, JSON.stringify({ email: _this.email, password: _this.password }), { 'Content-Type': 'application/json' }).then(function (data) {
                if (data['error']) {
                    reject(data['error']);
                    return;
                }
                _this.apply(data);
                resolve(_this);
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    UserModel.prototype.logout = function () {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] + '/session/logout', JSWorks.HTTPMethod.POST, JSON.stringify(_this.gist()), { 'Content-Type': 'application/json' }).then(function (data) {
                resolve();
            }).catch(function (err) {
                resolve();
            });
        });
    };
    UserModel.prototype.loggedIn = function () {
        return this.id !== undefined;
    };
    UserModel.prototype.query = function (params) {
        return _super.prototype.query.call(this, params);
    };
    UserModel.prototype.create = function () {
        return _super.prototype.create.call(this);
    };
    UserModel.prototype.read = function (id) {
        return _super.prototype.read.call(this, id);
    };
    UserModel.prototype[_a = 'delete'] = function () {
        return _super.prototype.delete.call(this);
    };
    UserModel.prototype.update = function () {
        return _super.prototype.update.call(this);
    };
    return UserModel;
}(AbstractModel_1.AbstractModel));
__decorate([
    JSWorks.ModelField,
    JSWorks.ModelPrimaryKey,
    __metadata("design:type", Number)
], UserModel.prototype, "id", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", Number)
], UserModel.prototype, "role", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], UserModel.prototype, "email", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], UserModel.prototype, "first_name", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], UserModel.prototype, "last_name", void 0);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String),
    __metadata("design:paramtypes", [String])
], UserModel.prototype, "about", null);
__decorate([
    JSWorks.ModelField,
    __metadata("design:type", String)
], UserModel.prototype, "password", void 0);
__decorate([
    JSWorks.ModelQueryMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], UserModel.prototype, "query", null);
__decorate([
    JSWorks.ModelCreateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], UserModel.prototype, "create", null);
__decorate([
    JSWorks.ModelReadMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Number]),
    __metadata("design:returntype", Promise)
], UserModel.prototype, "read", null);
__decorate([
    JSWorks.ModelDeleteMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], UserModel.prototype, _a, null);
__decorate([
    JSWorks.ModelUpdateMethod,
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", Promise)
], UserModel.prototype, "update", null);
UserModel = __decorate([
    JSWorks.Model,
    __metadata("design:paramtypes", [])
], UserModel);
exports.UserModel = UserModel;
var _a;
//# sourceMappingURL=UserModel.js.map