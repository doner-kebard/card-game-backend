FROM alpine:edge
RUN apk add --no-cache firefox-esr nodejs nodejs-npm dbus xvfb bash
RUN npm install -g testcafe
WORKDIR /usr/src/app
RUN Xvfb :0 &
RUN export DISPLAY=:0
COPY . /usr/src/app
ENTRYPOINT ["bash", "/usr/src/app/entry.sh"]
CMD /bin/true
