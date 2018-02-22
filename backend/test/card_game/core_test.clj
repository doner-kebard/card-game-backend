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
  [[10 0] [0 10] [0 0] [0 0] [0 0]]
  (-> (new-game)
      (play-card 0 0 0)
      (play-card 1 1 1)
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

; Game tracks correctly who still has to play
(expect
  [true true]
  (-> (new-game)
      :play-wanted))

(expect
  [false true]
  (-> (new-game)
      (play-card 0 0 0)
      :play-wanted))

(expect
  [true false]
  (-> (new-game)
      (play-card 1 0 0)
      :play-wanted))

(expect
  [true true]
  (-> (new-game)
      (play-card 0 0 0)
      (play-card 1 0 0)
      :play-wanted))

(expect
  [true true]
  (-> (new-game)
      (play-card 1 0 0)
      (play-card 0 0 0)
      :play-wanted))

(expect
  [false true]
  (-> (new-game)
      (play-card 1 0 0)
      (play-card 0 0 0)
      (play-card 0 0 0)
      :play-wanted))

; Can't play a card when you were not supposed to
(expect
  {:error "Out of turn play"}
  (-> (new-game)
      (play-card 0 0 0)
      (play-card 0 0 0)))

; Stores next-play
(expect
  {:player 0 :index 0 :row 0}
  (-> (new-game)
      (play-card 0 0 0)
      (get-in [:next-play 0])))
(expect
  {:player 1 :index 2 :row 3}
  (-> (new-game)
      (play-card 0 0 0)
      (play-card 1 1 1)
      (play-card 1 2 3)
      (get-in [:next-play 0])))
(expect
  [{:player 0 :index 0 :row 0} {:player 1 :index 1 :row 1}]
  (-> (new-game)
      (play-card 0 0 0)
      (play-card 1 1 1)
      :next-play))

; Doesn't updates game-state until both players played a card
(expect
  [[0 0] [0 0] [0 0] [0 0] [0 0]]
  (-> (new-game)
      (play-card 0 0 0)
      (get-points)))






