"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var TableController = (function () {
    function TableController() {
    }
    TableController.prototype.triggerRefresh = function () {
        this.component.selectedRow = undefined;
        var values = this.component.columns.getValues();
        this.component.columns.clear();
        return values;
    };
    TableController.prototype.onDOMInsert = function () {
        var _this = this;
        this.refresh();
        if (!this.component.toolbox) {
            return;
        }
        var button;
        button = this.view.DOMRoot.querySelector('.table-drop-filters-button');
        if (button) {
            button.addEventListener('click', function (event) {
                _this.clearFilterButtonState();
                var values = _this.triggerRefresh();
                _this.component.columns.setValues(values.map(function (column) {
                    var returning = { name: '', title: '' };
                    Object.keys(column).forEach(function (keyName) {
                        returning[keyName] = column[keyName];
                    });
                    returning.filter = undefined;
                    returning.order = undefined;
                    return returning;
                }));
            });
        }
        button = this.view.DOMRoot.querySelector('.table-add-button');
        if (button) {
            button.addEventListener('click', function () {
                if (_this.onAdd) {
                    _this.onAdd(_this.component);
                }
            });
        }
        button = this.view.DOMRoot.querySelector('.table-refresh-button');
        if (button) {
            button.addEventListener('click', function () {
                if (_this.onQuery) {
                    _this.onQuery(_this.component);
                }
                _this.refresh();
            });
        }
        window.setTimeout(function () {
            button = _this.view.DOMRoot.querySelector('.table-remove-button');
            if (!button) {
                return;
            }
            _this.view.DOMRoot.querySelector('.table-remove-button').addEventListener('click', function () {
                if (_this.view.DOMRoot.querySelector('.table-remove-button').hasClass('table-inactive-button')) {
                    return;
                }
                var windows = JSWorks.applicationContext.currentPage['view']
                    .DOMRoot.querySelector('#modal-root').component;
                var windowView = JSWorks.applicationContext.viewHolder.getView('DeleteDialogView');
                var yesButton = windowView.DOMRoot.querySelector('.dialog-yes');
                var noButton = windowView.DOMRoot.querySelector('.dialog-no');
                yesButton.removeEventListeners('click');
                yesButton.addEventListener('click', function () {
                    var data = _this.component.data.get(_this.component.selectedRow);
                    if (_this.onRemove) {
                        _this.onRemove(_this.component, data);
                    }
                    windows.closeLastWindow();
                });
                noButton.removeEventListeners('click');
                noButton.addEventListener('click', function () {
                    windows.closeLastWindow();
                });
                windows.openWindow(windowView);
            });
        }, 100);
    };
    TableController.prototype.refresh = function () {
        var paginator = this.view.DOMRoot.querySelector('.table-pager-text');
        if (paginator) {
            if (this.component.total === 0) {
                paginator.children.item(0).text = "\u041D\u0435\u0442 \u0437\u0430\u043F\u0438\u0441\u0435\u0439";
            }
            else {
                paginator.children.item(0).text = "\u041F\u043E\u043A\u0430\u0437\u0430\u043D\u044B \u0437\u0430\u043F\u0438\u0441\u0438 \u0441 " + (this.component.offset + 1) + " \u043F\u043E " + (((this.component.offset + this.component.limit) > this.component.total) ?
                    this.component.total : (this.component.offset + this.component.limit)) + "\n                        \u0438\u0437 " + this.component.total;
            }
        }
        var values = this.triggerRefresh();
        this.component.columns.setValues(values);
    };
    TableController.prototype.patchSorter = function (column, colData) {
        var _this = this;
        var sorter = column.querySelector('.table-column-sorter');
        if (!sorter || sorter['_tablePatched']) {
            return;
        }
        sorter.addEventListener('click', function (event) {
            event.stopPropagation();
            switch (colData.order) {
                case 'asc':
                    {
                        colData.order = 'desc';
                    }
                    break;
                case 'desc':
                    {
                        colData.order = undefined;
                    }
                    break;
                case undefined: {
                    colData.order = 'asc';
                }
            }
            if (_this.onQuery) {
                _this.onQuery(_this.component);
            }
            _this.refresh();
        });
        sorter['_tablePatched'] = true;
    };
    TableController.prototype.clearFilterButtonState = function () {
        var button = this.view.DOMRoot.querySelector('.table-filter button');
        button.removeEventListeners('click');
        button.parentNode.removeEventListeners('click');
        button.parentNode['_tablePatched'] = undefined;
    };
    TableController.prototype.patchFilter = function (column, colData) {
        var _this = this;
        var filter = column.querySelector('.table-column-filter');
        if (!filter || filter['_tablePatched']) {
            return;
        }
        filter.addEventListener('click', function (event) {
            event.stopPropagation();
            var input = _this.view.DOMRoot.querySelector('.table-filter');
            var boundingRect = filter.rendered.getBoundingClientRect();
            input.setStyleAttribute('left', Math.floor(boundingRect.left) + 'px');
            input.setStyleAttribute('top', Math.floor(boundingRect.top) + 'px');
            input.setStyleAttribute('display', 'inline-block');
            input.rendered.value = colData.filter || '';
            var button = _this.view.DOMRoot.querySelector('.table-filter button');
            if (!document.body['_tablePatched']) {
                document.body.addEventListener('click', function () {
                    input.setStyleAttribute('display', 'none');
                });
                document.body['_tablePatched'] = true;
            }
            if (!button.parentNode['_tablePatched']) {
                var dummyClickListener = function (event) {
                    event.stopPropagation();
                };
                var buttonClickListener = function () {
                    input.setStyleAttribute('display', 'none');
                    var value = input.querySelector('input').rendered.value;
                    var oldFilter = colData.filter;
                    if ((value || '').length > 0) {
                        colData.filter = value;
                    }
                    else {
                        colData.filter = undefined;
                    }
                    if (oldFilter === colData.filter) {
                        return;
                    }
                    _this.clearFilterButtonState();
                    if (_this.onQuery) {
                        _this.onQuery(_this.component);
                    }
                    _this.refresh();
                };
                button.addEventListener('click', buttonClickListener);
                button.parentNode.addEventListener('click', dummyClickListener);
                button.parentNode['_tablePatched'] = true;
            }
        });
        filter['_tablePatched'] = true;
    };
    TableController.prototype.patchCells = function () {
        var _this = this;
        this.view.DOMRoot.querySelectorAll('.table-cell').forEach(function (cell) {
            var columnIndex = parseInt(cell.getAttribute('column'), 10);
            var column = _this.component.columns.get(columnIndex);
            var href = cell.querySelector('a');
            if (href) {
                href.removeEventListeners('click');
                href.addEventListener('click', function (event) {
                    event.preventDefault();
                    var propName = column.foreignKey.valueKey || column.name;
                    if (typeof column.foreignKey.route === 'string') {
                        column.foreignKey.route = JSWorks.applicationContext.routeHolder.getRoute(column.foreignKey.route);
                    }
                    column.foreignKey.route.fire((_a = {}, _a[propName] = _this.component.data
                        .get(parseInt(cell.getAttribute('row'), 10))[propName], _a));
                    var _a;
                });
            }
            cell.toggleClass('cursor-pointer', _this.component.onCellClick !== undefined);
            cell.removeEventListeners('click');
            cell.addEventListener('click', function (event) {
                if (_this.component.onCellClick) {
                    _this.component.onCellClick(_this.component, parseInt(cell.getAttribute('row'), 10), parseInt(cell.getAttribute('column'), 10), _this.component.data.get(parseInt(cell.getAttribute('row'), 10)));
                }
                if (!_this.component.selectable || _this.component.isEditing) {
                    return;
                }
                _this.view.DOMRoot.querySelectorAll('.table-cell').forEach(function (anyCell) {
                    anyCell.toggleClass('table-cell-selected', false);
                });
                var row = parseInt(cell.getAttribute('row'), 10);
                _this.component.selectedRow = row;
                _this.view.DOMRoot.querySelectorAll(".table-cell[row=\"" + row + "\"]").forEach(function (rowCell) {
                    if (rowCell.hasClass('table-cell-title')) {
                        return;
                    }
                    rowCell.toggleClass('table-cell-selected', true);
                });
                cell.rendered.dispatchEvent(new Event('mouseover'));
            });
            cell.removeEventListeners('mouseover');
            cell.addEventListener('mouseover', function () {
                if (_this.component.isEditing) {
                    return;
                }
                var highlightCell = function (cell) {
                    if (cell.hasClass('table-cell-title') || cell.hasClass('table-cell-selected')) {
                        return;
                    }
                    cell.toggleClass('table-cell', true);
                    cell.toggleClass('table-cell-hover', true);
                };
                _this.view.DOMRoot.querySelectorAll('.table-cell').forEach(function (anyCell) {
                    if (anyCell.getAttribute('row') === cell.getAttribute('row') ||
                        anyCell.getAttribute('column') === cell.getAttribute('column')) {
                        highlightCell(anyCell);
                        return;
                    }
                    anyCell.toggleClass('table-cell-hover', false);
                });
            });
        });
    };
    TableController.prototype.setupInput = function (column, cell, text) {
        var _this = this;
        var input = JSWorks.applicationContext.serviceHolder.
            getServiceByName('SimpleVirtualDOM').createElement('INPUT');
        input.setAttribute('value', text);
        input.addEventListener('blur', function () {
            _this.component.isEditing = false;
            var data = _this.component.data.get(parseInt(cell.getAttribute('row'), 10));
            data[column.name] = input.rendered.value;
            _this.refresh();
            if (_this.onCellChange) {
                _this.onCellChange(_this.component, data);
            }
        });
        window.setTimeout(function () {
            input.rendered.focus();
        }, 10);
        return input;
    };
    TableController.prototype.setupSelect = function (column, cell, text) {
        var _this = this;
        var virtualDOM = JSWorks.applicationContext.serviceHolder.
            getServiceByName('SimpleVirtualDOM');
        var select = virtualDOM.createElement('SELECT');
        (column.selectList || []).forEach(function (optionText) {
            var option = virtualDOM.createElement('OPTION');
            option.appendChild(virtualDOM.createTextElement(optionText));
            if (optionText === text) {
                option.setAttribute('selected', 'selected');
            }
            var onBlur = function () {
                _this.component.isEditing = false;
                var data = _this.component.data.get(parseInt(cell.getAttribute('row'), 10));
                data[column.name] = select.rendered.value;
                _this.refresh();
                if (_this.onCellChange) {
                    _this.onCellChange(_this.component, data);
                }
            };
            select.addEventListener('blur', onBlur);
            select.addEventListener('change', onBlur);
            window.setTimeout(function () {
                select.rendered.focus();
            }, 10);
            select.appendChild(option);
        });
        return select;
    };
    TableController.prototype.patchCellEvents = function () {
        var _this = this;
        this.view.DOMRoot.querySelectorAll('.table-cell').forEach(function (cell) {
            if (cell['_tablePatched']) {
                return;
            }
            var columnIndex = parseInt(cell.getAttribute('column'), 10);
            var column = _this.component.columns.get(columnIndex);
            if (column.type === 'button') {
                var button = cell.querySelector('button');
                button.removeEventListeners('click');
                button.addEventListener('click', function () {
                    if (column.onButtonClick) {
                        column.onButtonClick(_this.component, _this.component.data.get(parseInt(cell.getAttribute('row'), 10)));
                    }
                });
            }
            if (column.canEdit) {
                cell.addEventListener('dblclick', function () {
                    _this.component.isEditing = true;
                    var text = _this.component.data.get(parseInt(cell.getAttribute('row'), 10))[column.name];
                    var viewEval = cell.querySelector('view-eval');
                    if (!viewEval) {
                        return;
                    }
                    viewEval.removeChildren();
                    switch (column.type) {
                        case 'select':
                            {
                                viewEval.appendChild(_this.setupSelect(column, cell, text));
                            }
                            break;
                        default:
                            {
                                viewEval.appendChild(_this.setupInput(column, cell, text));
                            }
                            break;
                    }
                });
            }
            cell['_tablePatched'] = true;
        });
    };
    TableController.prototype.patchColumnWidths = function () {
        var _this = this;
        var items = this.view.DOMRoot.querySelectorAll('.table > view-for > view-item');
        var width = 0;
        var undefinedWidths = 0;
        this.component.columns.getValues().forEach(function (col) {
            if (col.width) {
                width += col.width;
            }
            else {
                undefinedWidths++;
            }
        });
        items.forEach(function (item, index) {
            var col = _this.component.columns.get(index);
            if (!col) {
                return;
            }
            var grow = col.width;
            if (!grow) {
                grow = (1.0 - width) / undefinedWidths;
            }
            item.setStyleAttribute('flex-grow', String(grow * 100).replace(',', '.'));
        });
    };
    TableController.prototype.patchPager = function () {
        var _this = this;
        var pager = this.view.DOMRoot.querySelector('.table-pager');
        if (!pager || pager['_tablePatched']) {
            return;
        }
        var query = function () {
            if (_this.onQuery) {
                _this.onQuery(_this.component);
            }
            _this.refresh();
        };
        pager.querySelector('.table-pager-start').addEventListener('click', function () {
            if (_this.component.offset === 0) {
                return;
            }
            _this.component.offset = 0;
            query();
        });
        pager.querySelector('.table-pager-back').addEventListener('click', function () {
            if (_this.component.offset - _this.component.limit < 0) {
                return;
            }
            _this.component.offset -= _this.component.limit;
            query();
        });
        pager.querySelector('.table-pager-forward').addEventListener('click', function () {
            if (_this.component.offset + _this.component.limit >= _this.component.total) {
                return;
            }
            _this.component.offset += _this.component.limit;
            query();
        });
        pager.querySelector('.table-pager-end').addEventListener('click', function () {
            var newVal = Math.floor(_this.component.total / _this.component.limit) * _this.component.limit;
            if (_this.component.offset === newVal) {
                return;
            }
            _this.component.offset = newVal;
            query();
        });
        pager['_tablePatched'] = true;
    };
    TableController.prototype.onUpdate = function () {
        var _this = this;
        this.view.DOMRoot.querySelectorAll('.table-column').forEach(function (column) {
            var colData = _this.component.columns.get(parseInt(column.getAttribute('column')));
            _this.patchSorter(column, colData);
            _this.patchFilter(column, colData);
        });
        this.patchCells();
        this.patchCellEvents();
        this.patchColumnWidths();
        this.patchPager();
    };
    return TableController;
}());
TableController = __decorate([
    JSWorks.Controller
], TableController);
exports.TableController = TableController;
//# sourceMappingURL=TableController.js.map