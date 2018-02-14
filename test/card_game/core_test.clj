(ns card-game.core-test
  (:use expectations
        card-game.core))

; Game can be created
(expect
  #(not (nil? %))
  (new-game))

; Game contains two players
(expect
  2
  (-> (new-game)
      :players
      count))

; Game contains five rows
(expect
  5
  (-> (new-game)
      :rows
      count))

; Game rows are empty initially
(expect
  (repeat 5 [])
  (-> (new-game)
      :rows))

; Players start with 12 cards in hand
(expect
  12
  (-> (new-game)
      :players
      first
      :hand
      count))
(expect
  12
  (-> (new-game)
      :players
      second
      :hand
      count))

; Players can be made to start with a different number of cards
(expect
  3
  (-> (new-game 3)
      :players
      first
      :hand
      count))

; Players are generated with default cards of power 10
(expect
  (repeat 12 10)
  (map :power
       (-> (new-game)
           :players
           first
           :hand)))

; Playing a card to a row makes the card appear on that row only
(expect
  [nil nil nil 10 nil]
  (map #(:power (get % 0))
       (-> (new-game)
           (play-card 1 1 3)
           :rows)))
(expect
  [nil 10 nil nil nil]
  (map #(:power (get % 0))
       (-> (new-game)
           (play-card 1 1 1)
           :rows)))

; Playing a card many times makes it appear that many times in the row
(expect
  [10 10 10 10]
  (map :power 
       (-> (new-game)
           (play-card 1 1 3)
           (play-card 1 1 3)
           (play-card 1 1 3)
           (play-card 1 1 3)
           :rows
           (get 3))))

; Playing a card makes the card dissappear from the hand
(expect
  11
  (-> (new-game)
      (play-card 1 1 1)
      :players
      (nth 1)
      :hand
      count))

; Playing a card does not affect other players' hand
(expect
  12
  (-> (new-game)
      (play-card 1 1 1)
      :players
      (nth 0)
      :hand
      count))

; Altering a card works
(expect
  [15 10 10 10 10 10 10 10 10 10 10 15]
  (map :power
       (-> (new-game)
           (alter-card 0 0 {:power 15})
           (alter-card 0 11 {:power 15})
           :players
           (nth 0)
           :hand)))

; Playing altered card goes correctly
(expect
  15
  (-> (new-game)
      (alter-card 0 0 {:power 15})
      (play-card 0 0 0)
      :rows (get 0) (get 0)
      :power))
(expect
  [10 10 10 10 10 10 10 10 10 10 10]
  (map :power
       (-> (new-game)
           (alter-card 0 0 {:power 15})
           (play-card 0 0 0)
           :players
           (nth 0)
           :hand)))

; We can tell if a game is finished
(expect
  false
  (-> (new-game)
      (finished?)))
(expect
  true
  (-> (new-game 0)
      (finished?)))
(expect
  false
  (-> (new-game 1)
      (play-card 0 0 0)
      (finished?)))
(expect
  true
  (-> (new-game 1)
      (play-card 0 0 0)
      (play-card 1 0 0)
      (finished?)))

; We can get the current amount of points on different rows
(expect
 0
  (-> (new-game)
      (get-points)
      (get 0)
      (get 0)))
(expect
 [[0 0] [0 0] [0 0] [0 0] [0 0]]
  (-> (new-game)
      (get-points)))
(expect
  10
  (-> (new-game)
      (play-card 0 0 0)
      (get-points)
      (get 0)
      (get 0)))
(expect
 [[20 10] [0 0] [0 0] [0 10] [0 0]]
  (-> (new-game)
      (play-card 0 0 0)
      (play-card 1 0 0)
      (play-card 0 0 0)
      (play-card 1 0 3)
      (get-points)))

; Winner isn't set if game hasn't ended
(expect
  nil
  (-> (new-game)
      (winner)))

; Winner returns the winning player on a finished game, or 2 on a tie
(expect
  1
  (-> (new-game 2)
      (play-card 0 0 0)
      (play-card 1 0 1)
      (play-card 0 0 0)
      (play-card 1 0 2)
      (winner)))
(expect
  0
  (-> (new-game 2)
      (play-card 0 0 3)
      (play-card 1 0 2)
      (play-card 0 0 0)
      (play-card 1 0 2)
      (winner)))
(expect
  2
  (-> (new-game 2)
      (play-card 0 0 0)
      (play-card 1 0 0)
      (play-card 0 0 0)
      (play-card 1 0 0)
      (winner)))
