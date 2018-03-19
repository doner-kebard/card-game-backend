#!/bin/bash

XVFB_SCREEN_WIDTH=${SCREEN_WIDTH-1280}
XVFB_SCREEN_HEIGHT=${SCREEN_HEIGHT-720}

dbus-daemon --session --fork
Xvfb :1 -screen 0 "${XVFB_SCREEN_WIDTH}x${XVFB_SCREEN_HEIGHT}x24" >/dev/null 2>&1 &
export DISPLAY=:1.0
fluxbox >/dev/null 2>&1 &

cd /tests

npm install

if [[ "$1" == "test" ]]; then
    node /opt/testcafe/bin/testcafe.js --ports 1337,1338 firefox /tests/*.js -S -s screenshots
elif [[ "$1" == "test-delayed" ]]; then
    node /opt/testcafe/bin/testcafe.js --ports 1337,1338 firefox /tests/*.js --app 'true' --app-init-delay 20000 -S -s screenshots
else
    eval "$@"
fi
