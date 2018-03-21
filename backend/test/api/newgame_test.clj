(ns api.newgame-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as api]
            [configs.hands :as hands]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect sanity-check
  (let [game (api/create-game)]
    (expect
      #(contains? % :game-id)
      game)
    (expect
      #(contains? % :player-id)
      game)
    (expect
      #(= (count (:hand %)) (count hands/default-hand))
      game)
    (expect
      #(= 5 (count (:rows %)))
      game)))

(defexpect joining-a-game
  (let [game (api/create-game)
        opponent (api/add-player (:game-id game))]
    (expect
      #(contains? % :player-id)
      opponent)

    (expect
      #(not (contains? % :error))
      opponent)

    (expect
      true
      (= (:game-id game) (:game-id opponent)))

    ; A third player causes an error
    (expect
      {:error messages/too-many-players}
      (api/add-player (:game-id game)))

    (expect
      false
      (= (:player-id game) (:player-id opponent)))))
