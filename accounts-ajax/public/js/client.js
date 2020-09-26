"use strict";

let module = angular.module('AccountModule', ['ngResource']);
let uri = "http://localhost:9000/api";

module.factory('AccountService', function($resource) {
    return $resource(uri + "/account");
});

module.controller('AccountController', function(AccountService) {
    this.createAccount = function(account) {
        AccountService.save({}, account);
    };
});