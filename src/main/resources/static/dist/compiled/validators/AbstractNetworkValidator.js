"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var AbstractNetworkValidator = (function () {
    function AbstractNetworkValidator() {
    }
    AbstractNetworkValidator.prototype.intercept = function (args) {
        var _this = this;
        return new Promise(function (resolve, reject) {
            var jsonParser = JSWorks.applicationContext.serviceHolder
                .getServiceByName('JSONParser');
            var page = JSWorks.applicationContext.currentPage;
            page.loading = true;
            jsonParser.parseURLAsync(JSWorks.config['backendURL'] + _this.relUrl, JSWorks.HTTPMethod.POST, JSON.stringify({ value: args['value'] }), { 'Content-Type': 'application/json' }).then(function (result) {
                page.loading = false;
                if (!(result instanceof Array)) {
                    result = [result];
                }
                if (result.some(function (entry) {
                    if (typeof entry !== 'object') {
                        return true;
                    }
                    return entry['status'] === 'error';
                })) {
                    reject(result.map(function (entry) {
                        return entry['msg'];
                    }));
                    return;
                }
                resolve();
            }).catch(function (err) {
                page.loading = false;
                reject((err || {}).message || String(err));
            });
        });
    };
    return AbstractNetworkValidator;
}());
exports.AbstractNetworkValidator = AbstractNetworkValidator;
//# sourceMappingURL=AbstractNetworkValidator.js.map