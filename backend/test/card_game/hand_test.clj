(ns card-game.hand-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]))

(defexpect starting-hand
  ; Players start with 12 cards in hand
  (expect
    #(= (count (:hand %)) 12)
    (-> (core/new-game)
        :players
        first))

  (expect
    #(= (count (:hand %)) 12)
    (-> (core/new-game)
        :players
        second))

  ; Players can be made to start with a different number of cards
  (expect
    #(= (count (:hand %)) 3)
    (-> (core/new-game 3)
        :players
        second))

  ; Players are generated with default cards of power 10
  (expect
    (repeat 12 10)
    (map :power
         (-> (core/new-game)
             :players
             first
             :hand))))


(defexpect playing-cards
  ; Playing a card makes the card dissappear from the hand
  (expect
    #(= (count (:hand %)) 11)
    (-> (core/new-game)
        (core/play-card 0 2 1)
        (core/play-card 1 1 1)
        :players
        second))

  ; Playing a card does not affect other players' hand
  (expect
    [15 10 10 10 10 10 10 10 10 10 10]
    (map :power
         (-> (core/new-game)
             (core/alter-card [:players 1 :hand 0] {:power 15})
             (core/play-card 0 0 0)
             (core/play-card 1 1 1)
             :players
             (nth 1)
             :hand))))
