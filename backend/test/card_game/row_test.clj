(ns card-game.row-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]))

(defexpect initial-state
  ; Game rows are empty initially
  (expect
    (repeat 5 [])
    (-> (core/new-game)
        :rows)))

(defexpect card-playing
  ; Playing a card to a row makes the card appear on that row only
  (expect
    [nil nil nil 10 nil]
    (map #(:power (get % 0))
         (-> (core/new-game)
             (core/play-card 1 1 3)
             (core/play-card 0 1 3)
             :rows)))
  (expect
    [10 10 nil nil nil]
    (map #(:power (get % 0))
         (-> (core/new-game)
             (core/play-card 1 1 1)
             (core/play-card 0 0 0)
             :rows)))

  ; Playing a card many times makes it appear that many times in the row
  (expect
    [10 10 10 10]
    (map :power 
         (-> (core/new-game)
             (core/play-card 1 1 3)
             (core/play-card 0 0 0)
             (core/play-card 1 1 3)
             (core/play-card 0 0 0)
             (core/play-card 1 1 3)
             (core/play-card 0 0 0)
             (core/play-card 1 1 3)
             (core/play-card 0 0 2)
             :rows
             (get 3)))))
