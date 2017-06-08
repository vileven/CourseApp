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
Object.defineProperty(exports, "__esModule", { value: true });
var QueryBuilder_1 = require("../../helpers/QueryBuilder");
var AbstractAuthorizingController_1 = require("../AbstractAuthorizingController");
var AbstractAdminPageController = (function (_super) {
    __extends(AbstractAdminPageController, _super);
    function AbstractAdminPageController() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    AbstractAdminPageController.prototype.query = function (query) {
        return JSWorks.applicationContext.modelHolder.getModel(this.modelName).query(query);
    };
    AbstractAdminPageController.prototype.update = function (data) {
        return JSWorks.applicationContext.modelHolder.getModel(this.modelName).from(data).update();
    };
    AbstractAdminPageController.prototype['delete'] = function (data) {
        return JSWorks.applicationContext.modelHolder.getModel(this.modelName).from(data).delete();
    };
    AbstractAdminPageController.prototype.onFormOpen = function (form) {
        return Promise.resolve();
    };
    AbstractAdminPageController.prototype.onNavigate = function () {
        var _this = this;
        _super.prototype.onNavigate.call(this);
        var element = this.view.DOMRoot.querySelector("#table");
        this.table = element.component;
        this.table.controller.onQuery = function (table) {
            table.loading = true;
            _this.query(QueryBuilder_1.QueryBuilder.build(table)).then(function (result) {
                table.data.setValues(result);
                table.total = (result[0] || { total: 0 }).total;
                table.columns.update();
                table.controller.refresh();
                table.loading = false;
            }).catch(function (err) {
                table.loading = false;
                table.error = err;
            });
        };
        this.table.controller.onCellChange = function (table, data) {
            table.loading = true;
            _this.update(data).then(function () {
                _this.table.controller.onQuery(table);
            });
        };
        this.table.controller.onRemove = function (table, data) {
            table.loading = true;
            _this.delete(data).then(function () {
                _this.table.controller.onQuery(table);
            });
        };
        this.table.controller.onAdd = function (table) {
            var windows = JSWorks.applicationContext.currentPage['view']
                .DOMRoot.querySelector('#modal-root').component;
            var windowView = JSWorks.applicationContext.viewHolder.getView(_this.addFormName);
            var form = windowView.DOMRoot.querySelector('form-for');
            form.clear();
            form.onSuccess = function () {
                windows.closeLastWindow();
                _this.table.controller.onQuery(table);
                return true;
            };
            var closeButton = windowView.DOMRoot.querySelector('.button-close');
            closeButton.addEventListener('click', function () {
                form.clear();
                windows.closeLastWindow();
            });
            _this.onFormOpen(form).then(function () {
                windows.openWindow(windowView);
            }).catch(function (err) {
                table.error = err;
            });
        };
        this.setup();
        this.table.controller.onQuery(this.table);
    };
    return AbstractAdminPageController;
}(AbstractAuthorizingController_1.AbstractAuthorizingController));
exports.AbstractAdminPageController = AbstractAdminPageController;
//# sourceMappingURL=AbstractAdminPageController.js.map