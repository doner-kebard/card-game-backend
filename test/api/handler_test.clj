(ns api.handler-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [ring.mock.request :as mock]
            [api.handler :as handler]
            [configs.messages :as messages]
            [clojure.string :as string]))

(def ^:private cors-headers
  {"Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "OPTIONS"})

(def new-game (handler/handler (mock/request :post "/lobby")))

(defexpect handler-lobby-request
  (expect cors-headers (:headers new-game))
  (expect 200 (:status new-game))
  (expect messages/no-opp (:lobby-status (:body new-game)))
  (expect true (number? (:game-id (:body new-game))))
  (expect true (contains? (:body new-game) :player-id)))


(def joined-game (handler/handler (mock/request :post (string/join ["/games/" (:game-id (:body new-game))]))))

(defexpect handler-join-games-request
  (expect cors-headers (:headers joined-game))
  (expect 200 (:status joined-game))
  (expect (:game-id (:body new-game)) (:game-id (:body new-game)))
  (expect [0 0] (:scores (:body joined-game)))
  (expect messages/play (:game-status (:body joined-game)))
  (expect nil (:winner (:body joined-game)))
  (expect '(:game-id :player-id :cards :rows :scores :game-status :winner)
          (keys (:body joined-game))))

(defexpect handler-get-games-request
  (let [game (handler/handler (mock/request :get (string/join ["/games/" (:game-id (:body joined-game)) "/player/" (:player-id (:body joined-game))])))]
    (expect cors-headers (:headers game))
    (expect 200 (:status game))
    (expect (:game-id (:body joined-game)) (:game-id (:body game)))
    (expect (:player-id (:body joined-game)) (:player-id (:body game)))
    (expect [0 0] (:scores (:body game)))
    (expect messages/play (:game-status (:body game)))
    (expect nil (:winner (:body game)))
    (expect '(:game-id :player-id :cards :rows :scores :game-status :winner)
            (keys (:body game)))))

(defexpect handler-play-request
  (let [game (handler/handler (-> (mock/request :post (string/join ["/games/" (:game-id (:body new-game)) "/player/" (:player-id (:body new-game))]))
                                  (mock/json-body {:index 0 :row 0})))]
    (expect cors-headers (:headers game))
    (expect 200 (:status game))
    (expect (:game-id (:body new-game)) (:game-id (:body game)))
    (expect (:player-id (:body new-game)) (:player-id (:body game)))
    (expect messages/wait (:game-status (:body game)))
    (expect '(:game-id :player-id :cards :rows :scores :game-status :winner)
            (keys (:body game)))))

(defexpect handler-not-found
  (let [not-found (handler/handler (mock/request :get "/not-a-route"))]
    (expect cors-headers (:headers not-found))
    (expect 400 (:status not-found))
    (expect "Page not found" (:error (:body not-found)))))
