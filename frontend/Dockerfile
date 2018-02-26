FROM alpine
RUN apk add --no-cache yarn python3
WORKDIR /usr/src/app
ENTRYPOINT ["sh", "entry.sh"]
CMD yarn serve
