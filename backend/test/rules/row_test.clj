(ns rules.row-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [test-hand :as hand]
            [autoplay :as autoplay]))

(defexpect initial-state
  ; Game rows are empty initially
  (expect
    (repeat 5 [])
    (-> (create-game/new-game)
        :rows)))

(defexpect card-playing
  ; Playing a card to a row makes the card appear on that row only
  (expect
    [nil nil nil (hand/power-of-nth 1) nil]
    (map #(:power (get % 0))
         (-> (create-game/new-game)
             (play-card/play-card 1 1 3)
             (play-card/play-card 0 1 3)
             :rows)))
  (expect
    [(hand/power-of-nth 0) (hand/power-of-nth 1) nil nil nil]
    (map #(:power (get % 0))
         (-> (create-game/new-game)
             (play-card/play-card 1 1 1)
             (play-card/play-card 0 0 0)
             :rows)))

  ; Playing a card many times makes it appear that many times in the row
  (expect
    [(hand/power-of-nth 1)
     (hand/power-of-nth 2)
     (hand/power-of-nth 3)
     (hand/power-of-nth 4)]
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
