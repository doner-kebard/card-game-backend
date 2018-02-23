(ns card-game.core-test
  (:require [expectations.clojure.test :refer :all]
        [card-game.core :refer :all]
        [card-game.victory-conditions :refer :all]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (new-game)))

(defexpect finished-game
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
        (play-card 1 0 0))))

(defexpect points-on-rows
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
        (get-points))))

; Winner isn't set if game hasn't ended
(expect
  nil
  (-> (new-game)
      (winner)))

(defexpect winning-player
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
        (winner))))

(defexpect track-player
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
        :play-wanted)))

(defexpect ot-of-turn
  ; Can't play a card when you were not supposed to
  (expect
    {:error "Out of turn play"}
    (-> (new-game)
        (play-card 0 0 0)
        (play-card 0 0 0))))

(defexpect next-play
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
        :next-play)))

(defexpect update-only-when-both-play
  ; Doesn't updates game-state until both players played a card
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (new-game)
        (play-card 0 0 0)
        (get-points))))
