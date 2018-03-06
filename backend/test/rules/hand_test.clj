(ns rules.hand-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [configs.hand :as hand]))

(defexpect starting-hand
  ; Players start with expected hand
  (expect
    (hand/ini-hand)
      (-> (create-game/new-game)
          :players
          first
          :hand))
  (expect
    (hand/ini-hand)
      (-> (create-game/new-game)
          :players
          second
          :hand)))

(defexpect playing-cards
  ; Playing a card makes the card dissappear from the hand
  (expect
    #(= (count (:hand %)) (- (count (hand/ini-hand)) 1))
    (-> (create-game/new-game)
        (play-card/play-card 0 2 1)
        (play-card/play-card 1 1 1)
        :players
        second)))
