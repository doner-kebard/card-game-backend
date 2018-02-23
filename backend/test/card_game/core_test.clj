(ns card-game.core-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (core/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (core/new-game)))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory-conditions/finished? %))
    (core/new-game))

  (expect
    victory-conditions/finished?
    (core/new-game 0))

  (expect
    #(not (victory-conditions/finished? %))
    (-> (core/new-game 1)
        (core/play-card 0 0 0)))

  (expect
    victory-conditions/finished?
    (-> (core/new-game 1)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0))))

(defexpect points-on-rows
  ; We can get the current amount of points on different rows
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (victory-conditions/get-points)))

  (expect
    [[10 0] [0 10] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (victory-conditions/get-points)))

  (expect
    [[20 10] [0 0] [0 0] [0 10] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0)
        (core/play-card 0 0 0)
        (core/play-card 1 0 3)
        (victory-conditions/get-points))))

; Winner isn't set if game hasn't ended
(expect
  nil
  (-> (core/new-game)
      (victory-conditions/winner)))

(defexpect winning-player
  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    1
    (-> (core/new-game 2)
        (core/play-card 0 0 0)
        (core/play-card 1 0 1)
        (core/play-card 0 0 0)
        (core/play-card 1 0 2)
        (victory-conditions/winner)))

  (expect
    0
    (-> (core/new-game 2)
        (core/play-card 0 0 3)
        (core/play-card 1 0 2)
        (core/play-card 0 0 0)
        (core/play-card 1 0 2)
        (victory-conditions/winner)))

  (expect
    2
    (-> (core/new-game 2)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0)
        (victory-conditions/winner))))

(defexpect track-player
  ; Game tracks correctly who still has to play
  (expect
    [true true]
    (-> (core/new-game)
        :play-wanted))

  (expect
    [false true]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        :play-wanted))

  (expect
    [true false]
    (-> (core/new-game)
        (core/play-card 1 0 0)
        :play-wanted))

  (expect
    [true true]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0)
        :play-wanted))

  (expect
    [true true]
    (-> (core/new-game)
        (core/play-card 1 0 0)
        (core/play-card 0 0 0)
        :play-wanted))

  (expect
    [false true]
    (-> (core/new-game)
        (core/play-card 1 0 0)
        (core/play-card 0 0 0)
        (core/play-card 0 0 0)
        :play-wanted)))

(defexpect ot-of-turn
  ; Can't play a card when you were not supposed to
  (expect
    {:error "Out of turn play"}
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 0 0 0))))

(defexpect next-play
  ; Stores next-play
  (expect
    {:player 0 :index 0 :row 0}
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (get-in [:next-play 0])))
  (expect
    {:player 1 :index 2 :row 3}
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (core/play-card 1 2 3)
        (get-in [:next-play 0])))
  (expect
    [{:player 0 :index 0 :row 0} {:player 1 :index 1 :row 1}]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        :next-play)))

(defexpect update-only-when-both-play
  ; Doesn't updates game-state until both players played a card
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (victory-conditions/get-points))))
