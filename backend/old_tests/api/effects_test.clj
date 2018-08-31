(ns api.effects-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as api]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect playing-effects
  ; Can play cards with effects and they act as expected
  (expect
    -100
    (let [game (api/create-game {:hands [[{:power 0} {:power 100}]
                                         [{:power 1} {:power 10 :add-power -100}]]})
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (api/play-card-as-player game-id player-id   0 0)
      (api/play-card-as-player game-id opponent-id 0 1)
      (api/play-card-as-player game-id player-id   0 1)
      (api/play-card-as-player game-id opponent-id 0 1 [:rows 0 :cards 0])
      (get-in (api/get-game game-id player-id)
              [:rows 0 :cards 0 :power]))))
