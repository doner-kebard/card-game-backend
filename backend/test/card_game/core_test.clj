(ns card-game.core-test
  (:use expectations
        card-game.core
        card-game.victory-conditions))

; Game can be created
(expect
  #(not (nil? %))
  (new-game))

; Game contains two players
(expect
  #(count (:players %))
  (new-game))

; We can tell if a game is finished
(expect
  #(not (finished? %))
  (new-game))

(expect
  finished?
  (new-game 0))

(expect
  #(not (finished? %))
  (-> (new-game 1)
      (play-card 0 0 0)))

(expect
  finished?
  (-> (new-game 1)
      (play-card 0 0 0)
      (play-card 1 0 0)))

; We can get the current amount of points on different rows
(expect
  [[0 0] [0 0] [0 0] [0 0] [0 0]]
  (-> (new-game)
      (get-points)))

(expect
  [[10 0] [0 0] [0 0] [0 0] [0 0]]
  (-> (new-game)
      (play-card 0 0 0)
      (get-points)))

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
