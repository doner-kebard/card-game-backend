(ns card-game.hand-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core.create-game :as create-game]
            [card-game.core.play-card :as play-card]
            [configs :as configs]))

(defexpect starting-hand
  ; Players start with expected hand
  (expect
    (configs/ini-hand)
      (-> (create-game/new-game)
          :players
          first
          :hand))
  (expect
    (configs/ini-hand)
      (-> (create-game/new-game)
          :players
          second
          :hand)))

(defexpect playing-cards
  ; Playing a card makes the card dissappear from the hand
  (expect
    #(= (count (:hand %)) (- (count (configs/ini-hand)) 1))
    (-> (create-game/new-game)
        (play-card/play-card 0 2 1)
        (play-card/play-card 1 1 1)
        :players
        second)))
