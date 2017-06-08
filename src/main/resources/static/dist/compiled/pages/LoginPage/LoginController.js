"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var CurrentUserHelper_1 = require("../../helpers/CurrentUserHelper");
var LoginController = (function () {
    function LoginController() {
    }
    LoginController.prototype.onCreate = function () {
        var _this = this;
        this.form = this.view.DOMRoot.querySelector('#LoginForm');
        this.form.submitCallback = function () {
            return _this.form.model
                .login()
                .then(function (result) {
                CurrentUserHelper_1.CurrentUserHelper.currentUser = Promise.resolve(JSWorks.applicationContext.modelHolder.getModel('UserModel').from(result));
                window.setTimeout(function () {
                    JSWorks.applicationContext.router.navigate(JSWorks.applicationContext.routeHolder.getRoute('MainRoute'), {});
                });
            }).catch(function (err) {
                _this.component.error = err;
                _this.form.model = JSWorks.applicationContext.modelHolder.getModel('UserModel').from();
            });
        };
    };
    LoginController.prototype.onNavigate = function (args) {
        CurrentUserHelper_1.CurrentUserHelper.currentUser.then(function (user) {
            if (user.loggedIn()) {
                user.logout();
            }
        });
        this.onCreate();
        this.component.email = undefined;
        if (args[':email']) {
            this.component.email = args[':email'];
            this.form.fields.forEach(function (field) {
                if (field.getAttribute('for') !== 'email') {
                    return;
                }
                field.value = args[':email'];
            });
        }
    };
    return LoginController;
}());
LoginController = __decorate([
    JSWorks.Controller
], LoginController);
exports.LoginController = LoginController;
//# sourceMappingURL=LoginController.js.map