cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [

  {
    "id": "io.samos.plugins.CipherPlugin.CipherPlugin",
    "file": "plugins/io.samos.plugins/www/CipherPlugin.js",
    "pluginId": "io.samos.plugins.CipherPlugin",
    "clobbers": [
      "SamosCipherPlugin"
    ]
  },
   {
      "id": "cordova-plugin-device.device",
      "file": "plugins/cordova-plugin-device/www/device.js",
      "pluginId": "cordova-plugin-device",
      "clobbers": [
        "device"
      ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
  "cordova-plugin-whitelist": "1.3.3",
  "io.samos.plugins.CipherPlugin": "1.0.0",
   "cordova-plugin-device": "2.0.2"

};
// BOTTOM OF METADATA
});