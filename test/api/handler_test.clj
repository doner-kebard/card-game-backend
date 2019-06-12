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

(defn new-game [] (handler/handler (mock/request :post "/lobby")))
(defn join-game [game-id] (handler/handler (mock/request :post (string/join ["/games/" game-id]))))

(defexpect handler-lobby-request
  (let [game (new-game)]
    (expect cors-headers (:headers game))
    (expect 200 (:status game))
    (expect messages/no-opp (:lobby-status (:body game)))
    (expect true (number? (:game-id (:body game))))
    (expect true (contains? (:body game) :player-id))))

(defexpect handler-join-games-request
  (let [game-id (:game-id (:body (new-game)))
        game (join-game game-id)]
    (expect cors-headers (:headers game))
    (expect 200 (:status game))
    (expect game-id (:game-id (:body game)))
    (expect [0 0] (:scores (:body game)))
    (expect messages/play (:game-status (:body game)))
    (expect nil (:winner (:body game)))
    (expect '(:game-id :player-id :cards :rows :scores :game-status :winner)
            (keys (:body game)))))

(defexpect handler-get-games-request
  (let [lobby (new-game)
        game-id (:game-id (:body lobby))
        game0 (handler/handler (mock/request :get (string/join ["/games/" game-id "/player/" (:player-id (:body lobby))])))]
    (expect cors-headers (:headers game0))
    (expect 200 (:status game0))
    (expect game-id (:game-id (:body game0)))
    (expect messages/no-opp (:game-status (:body game0)))
    (let [player2-id (:player-id (:body (join-game game-id)))
          game (handler/handler (mock/request :get (string/join ["/games/" game-id "/player/" player2-id])))]
      (expect cors-headers (:headers game))
      (expect 200 (:status game))
      (expect game-id (:game-id (:body game)))
      (expect player2-id (:player-id (:body game)))
      (expect [0 0] (:scores (:body game)))
      (expect messages/play (:game-status (:body game)))
      (expect nil (:winner (:body game)))
      (expect '(:game-id :player-id :cards :rows :scores :game-status :winner)
              (keys (:body game))))))

(defexpect handler-play-request
  (let [lobby (new-game)
        game-id (:game-id (:body lobby))
        player-id (:player-id (:body lobby))]
    (join-game game-id)
    (let [game (handler/handler (-> (mock/request :post (string/join ["/games/" game-id "/player/" player-id]))
                                    (mock/json-body {:index 0 :row 0})))]
      (expect cors-headers (:headers game))
      (expect 200 (:status game))
      (expect game-id (:game-id (:body game)))
      (expect player-id (:player-id (:body game)))
      (expect messages/wait (:game-status (:body game)))
      (expect '(:game-id :player-id :cards :rows :scores :game-status :winner)
              (keys (:body game))))))

(defexpect handler-not-found
  (let [not-found (handler/handler (mock/request :get "/not-a-route"))]
    (expect cors-headers (:headers not-found))
    (expect 400 (:status not-found))
    (expect "Page not found" (:error (:body not-found)))))
