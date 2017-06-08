"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var AbstractModel = (function () {
    function AbstractModel() {
        this.total = 0;
    }
    AbstractModel.prototype.query = function (params) {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] +
                ("/" + _this.controllerUrl + "/select"), JSWorks.HTTPMethod.POST, JSON.stringify(params), { 'Content-Type': 'application/json' }).then(function (data) {
                var models = [];
                data.entries.forEach(function (item) {
                    models.push(_this.from(item));
                    models[models.length - 1].total = data.total;
                });
                resolve(models);
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    AbstractModel.prototype.create = function () {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] +
                ("/" + _this.controllerUrl + "/create"), JSWorks.HTTPMethod.POST, JSON.stringify(_this.gist()), { 'Content-Type': 'application/json' }).then(function (data) {
                resolve(_this.from(data));
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    AbstractModel.prototype.read = function (id) {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] +
                ("/" + _this.controllerUrl + "/" + (id || _this.id)), JSWorks.HTTPMethod.GET).then(function (data) {
                _this.apply(data);
                resolve(_this);
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    AbstractModel.prototype.update = function () {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] +
                ("/" + _this.controllerUrl + "/update"), JSWorks.HTTPMethod.POST, JSON.stringify(_this.gist()), { 'Content-Type': 'application/json' }).then(function (data) {
                _this.apply(data);
                _this.setDirty(false);
                resolve(_this);
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    AbstractModel.prototype['delete'] = function () {
        var _this = this;
        return new Promise(function (resolve, reject) {
            _this.jsonParser.parseURLAsync(JSWorks.config['backendURL'] +
                ("/" + _this.controllerUrl + "/delete"), JSWorks.HTTPMethod.POST, JSON.stringify({ id: _this.id }), { 'Content-Type': 'application/json' }).then(function (data) {
                resolve(_this);
            }).catch(function (err) {
                reject(err);
            });
        });
    };
    AbstractModel.prototype.from = function (data) { return undefined; };
    ;
    return AbstractModel;
}());
exports.AbstractModel = AbstractModel;
//# sourceMappingURL=AbstractModel.js.map