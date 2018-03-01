(ns card-game.simultaneous-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [configs :as configs]))

(defexpect out-of-turn
  ; Can't play a card when you were not supposed to
  (expect
    {:error configs/out-of-turn}
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
        (get-in [:next-play 1])))
  ; Stores nil when a play is expected
  (expect
    [nil nil]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        :next-play))
  (expect
    [nil nil]
    (-> (core/new-game)
        :next-play))
  (expect
    nil
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (get-in [:next-play 1])))
  (expect
    nil
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (core/play-card 1 2 3)
        (get-in [:next-play 0]))))


(defexpect update-only-when-both-play
  ; Doesn't updates game-state until both players played a card
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (victory-conditions/get-points))))
