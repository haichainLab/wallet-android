cordova.define("io.samos.plugins.CipherPlugin.CipherPlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.getPubkey = function (arg0, success, error) {
    exec(success, error, 'CipherPlugin', 'getPubkey', [arg0]);
};

exports.signData = function (arg0, success, error) {
    exec(success, error, 'CipherPlugin', 'signData', [arg0]);
};
});
