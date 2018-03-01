(ns card-game.api-newgame-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.api.base :as api]
            [configs :as configs]))

(defexpect sanity-check
  (expect
    #(contains? % :game-id)
    (api/create-game))
  (expect
    #(contains? % :player-id)
    (api/create-game))
  (expect
    #(= (count (:hand %)) (count (configs/ini-hand)))
    (api/create-game))
  (expect
    #(= 5 (count (:rows %)))
    (api/create-game)))

(defexpect joining-a-game
  (expect
    #(contains? % :player-id)
    (-> (api/create-game)
        :game-id
        (api/add-player)))

  (expect
    #(not (contains? % :error))
    (-> (api/create-game)
        :game-id
        (api/add-player)))

  (expect
    #(= % (:game-id (api/add-player %)))
    (-> (api/create-game)
        :game-id))

  ; A third player causes an error
  (expect
    {:error configs/too-many-players}
    (-> (api/create-game)
        :game-id
        (api/add-player)
        :game-id
        (api/add-player)))

  (expect
    #(not (= (:player-id %) (:player-id (api/add-player (:game-id %)))))
    (api/create-game)))
