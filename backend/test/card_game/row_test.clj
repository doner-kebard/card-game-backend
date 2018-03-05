(ns card-game.row-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core.create-game :as create-game]
            [card-game.core.play-card :as play-card]
            [configs :as configs]
            [card-game.test-helper :as helper]))

(defexpect initial-state
  ; Game rows are empty initially
  (expect
    (repeat 5 [])
    (-> (create-game/new-game)
        :rows)))

(defexpect card-playing
  ; Playing a card to a row makes the card appear on that row only
  (expect
    [nil nil nil (helper/ini-hand-power 1) nil]
    (map #(:power (get % 0))
         (-> (create-game/new-game)
             (play-card/play-card 1 1 3)
             (play-card/play-card 0 1 3)
             :rows)))
  (expect
    [(helper/ini-hand-power 0) (helper/ini-hand-power 1) nil nil nil]
    (map #(:power (get % 0))
         (-> (create-game/new-game)
             (play-card/play-card 1 1 1)
             (play-card/play-card 0 0 0)
             :rows)))

  ; Playing a card many times makes it appear that many times in the row
  (expect
    [(helper/ini-hand-power 1)
     (helper/ini-hand-power 2)
     (helper/ini-hand-power 3)
     (helper/ini-hand-power 4)]
    (map :power 
         (-> (create-game/new-game)
             (play-card/play-card 1 1 3)
             (play-card/play-card 0 0 0)
             (play-card/play-card 1 1 3)
             (play-card/play-card 0 0 0)
             (play-card/play-card 1 1 3)
             (play-card/play-card 0 0 0)
             (play-card/play-card 1 1 3)
             (play-card/play-card 0 0 2)
             :rows
             (get 3)))))
