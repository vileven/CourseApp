"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var QueryBuilder = (function () {
    function QueryBuilder() {
    }
    QueryBuilder.build = function (table) {
        var orders = [];
        var filters = [];
        table.columns.getValues().forEach(function (column) {
            if (column.order !== undefined) {
                orders.push([column.name, column.order.toUpperCase()]);
            }
            if (column.filter !== undefined) {
                filters.push([column.name, column.filter]);
            }
        });
        return {
            limit: table.limit,
            offset: table.offset,
            orders: orders,
            filters: filters,
        };
    };
    return QueryBuilder;
}());
exports.QueryBuilder = QueryBuilder;
//# sourceMappingURL=QueryBuilder.js.map