var config = {};
var _is_config_loaded = false;

var req = new XMLHttpRequest();
req.open("GET", "/config/data/config.yml");
req.responseType = "text";
req.onload = function () {
    config = jsyaml.load(req.response);
    _is_config_loaded = true;
}
req.send();

function onConfigLoad(action) {
    if (_is_config_loaded) {
        action();
    } else {
        oldload = req.onload;
        req.onload = function () {
            oldload();
            action();
        }
    }
}
