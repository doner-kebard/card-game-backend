(ns rules.new-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory-conditions]
            [test-helper :as helper]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (create-game/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (create-game/new-game))

  ; Hand's not empty
  (expect
    false
    (-> (create-game/new-game)
        (get-in [:players 0 :hand])
        (empty?)))

  ; Rows begin empty
  (expect
    [[] [] [] [] []]
    (-> (create-game/new-game)
        :rows)))
