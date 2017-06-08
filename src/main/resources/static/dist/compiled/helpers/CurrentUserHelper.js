"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var CurrentUserHelper = (function () {
    function CurrentUserHelper() {
    }
    Object.defineProperty(CurrentUserHelper, "currentUser", {
        get: function () {
            var model = JSWorks.applicationContext.modelHolder.getModel('UserModel');
            if (!CurrentUserHelper._currentUser) {
                return new Promise(function (resolve, reject) {
                    model.current().then(function (user) {
                        CurrentUserHelper.currentUser = Promise.resolve(user);
                        resolve(user);
                    });
                });
            }
            return Promise.resolve(CurrentUserHelper._currentUser);
        },
        set: function (value) {
            value.then(function (result) {
                CurrentUserHelper._currentUser = result;
            });
        },
        enumerable: true,
        configurable: true
    });
    CurrentUserHelper.getInfo = function () {
        return new Promise(function (resolve, reject) {
            CurrentUserHelper
                .currentUser
                .then(function (user) {
                switch (user.role) {
                    case 0:
                        user.jsonParser.parseURLAsync(JSWorks.config['backendURL'] + '/admin/info', JSWorks.HTTPMethod.GET)
                            .then(function (data) { return resolve(data); });
                        break;
                    case 1:
                        user.jsonParser.parseURLAsync(JSWorks.config['backendURL'] + '/student/info', JSWorks.HTTPMethod.GET)
                            .then(function (data) {
                            console.log(data);
                            data = [
                                {
                                    course_id: 1,
                                    course_name: 'technopark 2 sem',
                                    group_id: 1,
                                    group_name: "AПО-21",
                                    subjects: [
                                        { subject_id: 1, subject_name: "DataBase", total: 80, mark_name: 'хор' },
                                        { subject_id: 2, subject_name: "Front-end", total: 100, mark_name: 'отл' },
                                        { subject_id: 3, subject_name: "Back-end", total: 50, mark_name: 'удовл' }
                                    ]
                                },
                                {
                                    course_id: 2,
                                    course_name: 'technopark 3 sem',
                                    group_id: 2,
                                    group_name: "AПО-31",
                                    subjects: [
                                        { subject_id: 4, subject_name: "Android", total: 100, mark_name: 'отл' },
                                        { subject_id: 5, subject_name: "Security", total: 89, mark_name: 'отл' },
                                        { subject_id: 6, subject_name: "Hightload", total: 49, mark_name: 'хор' }
                                    ]
                                }
                            ];
                            resolve(data);
                        });
                        break;
                    case 2:
                        user.jsonParser.parseURLAsync(JSWorks.config['backendURL'] + '/professor/info', JSWorks.HTTPMethod.GET)
                            .then(function (data) { return resolve(data); });
                        break;
                }
            });
        });
    };
    return CurrentUserHelper;
}());
exports.CurrentUserHelper = CurrentUserHelper;
//# sourceMappingURL=CurrentUserHelper.js.map