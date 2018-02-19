FROM clojure:alpine
WORKDIR /usr/src/app
COPY project.clj /usr/src/app
RUN lein deps
CMD lein expectations && lein ring server-headless
