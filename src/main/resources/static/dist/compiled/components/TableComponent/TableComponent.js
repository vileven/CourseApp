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
var TableComponent = (function () {
    function TableComponent() {
        this.title = 'Table';
        this.selectable = true;
        this.loading = false;
        /* public toolbox: boolean = true;
    
        public paginator: boolean = true; */
        this.offset = 0;
        this.limit = 20;
        this.total = 1483;
        this.isEditing = false;
        this._toolbox = true;
        this._paginator = true;
        this.columns = [];
        this.data = [];
    }
    Object.defineProperty(TableComponent.prototype, "toolbox", {
        get: function () {
            return this._toolbox;
        },
        set: function (value) {
            this.view.DOMRoot.querySelector('.table-toolbox').toggleClass('hidden', !value);
            this._toolbox = value;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(TableComponent.prototype, "paginator", {
        get: function () {
            return this._paginator;
        },
        set: function (value) {
            this.view.DOMRoot.querySelector('.table-pager').toggleClass('hidden', !value);
            this._paginator = value;
        },
        enumerable: true,
        configurable: true
    });
    return TableComponent;
}());
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", String)
], TableComponent.prototype, "title", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Boolean)
], TableComponent.prototype, "selectable", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Boolean)
], TableComponent.prototype, "loading", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", String)
], TableComponent.prototype, "error", void 0);
__decorate([
    JSWorks.ComponentProperty(),
    __metadata("design:type", Number)
], TableComponent.prototype, "selectedRow", void 0);
__decorate([
    JSWorks.ComponentCollectionProperty(),
    __metadata("design:type", Array)
], TableComponent.prototype, "columns", void 0);
__decorate([
    JSWorks.ComponentCollectionProperty(),
    __metadata("design:type", Array)
], TableComponent.prototype, "data", void 0);
TableComponent = __decorate([
    JSWorks.Component({ view: 'TableView', controller: 'TableController' })
], TableComponent);
exports.TableComponent = TableComponent;
//# sourceMappingURL=TableComponent.js.map