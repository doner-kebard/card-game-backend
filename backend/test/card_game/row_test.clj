(ns card-game.row-test
  (:use expectations
        card-game.core))

; Game rows are empty initially
(expect
  (repeat 5 [])
  (-> (new-game)
      :rows))

; Playing a card to a row makes the card appear on that row only
(expect
  [nil nil nil 10 nil]
  (map #(:power (get % 0))
       (-> (new-game)
           (play-card 1 1 3)
           (play-card 0 1 3)
           :rows)))
(expect
  [10 10 nil nil nil]
  (map #(:power (get % 0))
       (-> (new-game)
           (play-card 1 1 1)
           (play-card 0 0 0)
           :rows)))

; Playing a card many times makes it appear that many times in the row
(expect
  [10 10 10 10]
  (map :power 
       (-> (new-game)
           (play-card 1 1 3)
           (play-card 0 0 0)
           (play-card 1 1 3)
           (play-card 0 0 0)
           (play-card 1 1 3)
           (play-card 0 0 0)
           (play-card 1 1 3)
           (play-card 0 0 2)
           :rows
           (get 3))))
