(ns api.handler-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [ring.mock.request :as mock]
            [api.handler :as handler]
            [configs.messages :as messages]))

(def ^:private cors-headers
  {"Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "OPTIONS"})


(defexpect handler-lobby-request
  (let [new-game (handler/handler (mock/request :post "/lobby"))]
    (expect cors-headers (:headers new-game))
    (expect 200 (:status new-game))
    (expect messages/no-opp (:lobby-status (:body new-game)))
    (expect true (contains? (:body new-game) :player-id))
    (expect true (number? (:game-id (:body new-game))))))

(defexpect handler-not-found
  (let [not-found (handler/handler (mock/request :get "/not-a-route"))]
    (expect cors-headers (:headers not-found))
    (expect 400 (:status not-found))
    (expect "Page not found" (:error (:body not-found)))))
