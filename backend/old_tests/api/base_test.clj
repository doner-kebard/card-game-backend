(ns api.base-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as api]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect create-game

  (let [game (api/create-game)]
    ; Checking it has been created
    (expect
      #(contains? % :game-id)
      game)

    (expect
      #(contains? % :player-id)
      game)

    (expect
      #(= 5 (count (:rows %)))
      game)))

(defexpect add-player
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
