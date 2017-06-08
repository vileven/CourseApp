"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var CurrentUserHelper_1 = require("../helpers/CurrentUserHelper");
var AbstractAuthorizingController = (function () {
    function AbstractAuthorizingController() {
    }
    AbstractAuthorizingController.prototype.onNavigate = function (args) {
        var _this = this;
        this.view.DOMRoot.setStyleAttribute('display', 'none');
        this.component.loading = true;
        CurrentUserHelper_1.CurrentUserHelper.currentUser.then(function (user) {
            _this.view.DOMRoot.setStyleAttribute('display', 'inherit');
            _this.component.loading = false;
            if (!user.loggedIn()) {
                JSWorks.applicationContext.router.navigate(JSWorks.applicationContext.routeHolder.getRoute('LandingRoute'), {});
                return;
            }
        });
    };
    return AbstractAuthorizingController;
}());
exports.AbstractAuthorizingController = AbstractAuthorizingController;
//# sourceMappingURL=AbstractAuthorizingController.js.map