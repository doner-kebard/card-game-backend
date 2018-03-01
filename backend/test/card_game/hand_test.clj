(ns card-game.hand-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [configs :as configs]))

(defexpect starting-hand
  ; Players start with expected hand
  (expect
    (configs/ini-hand)
      (-> (core/new-game)
          :players
          first
          :hand))
  (expect
    (configs/ini-hand)
      (-> (core/new-game)
          :players
          second
          :hand)))

(defexpect playing-cards
  ; Playing a card makes the card dissappear from the hand
  (expect
    #(= (count (:hand %)) (- (count (configs/ini-hand)) 1))
    (-> (core/new-game)
        (core/play-card 0 2 1)
        (core/play-card 1 1 1)
        :players
        second)))
