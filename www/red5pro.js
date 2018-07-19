var exec = require('cordova/exec');

function red5pro() {}

//exports.coolMethod = function (arg0, success, error) {
//    exec(success, error, 'dcuptest', 'coolMethod', [arg0]);
//};

red5pro.prototype.record = function (arg0, callbackContext) {
    callbackContext = callbackContext || {};
    exec(callbackContext.success || null, callbackContext.error || null, 'Red5pro', 'record', [arg0]);
};



module.exports = new red5pro();