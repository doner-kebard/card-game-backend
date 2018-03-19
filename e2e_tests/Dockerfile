FROM alpine:edge

RUN apk --no-cache --repository http://dl-3.alpinelinux.org/alpine/edge/testing/ add \
    nodejs nodejs-npm chromium firefox\
    xwininfo xvfb dbus eudev ttf-freefont fluxbox git

RUN git clone --recursive --depth 10 https://github.com/DevExpress/testcafe /tmp/testcafe

WORKDIR /tmp/testcafe

RUN npm install -g gulp

RUN npm install && gulp fast-build

RUN mkdir -p /opt/testcafe && mv bin lib package.json /opt/testcafe/

WORKDIR /opt/testcafe

COPY entry.sh /opt/testcafe/

RUN npm install --production && \
    rm -rf /tmp/*

EXPOSE 1337 1338

ENTRYPOINT ["sh", "entry.sh"]
