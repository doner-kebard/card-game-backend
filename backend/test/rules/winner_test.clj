(ns rules.winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory-conditions]
            [configs.hand :as hand]))

(defn play-a-game-helper
  [strategy1 strategy2]
  (loop [game-state (create-game/new-game)
         iteration (count (hand/ini-hand))]
      (if (= 0 iteration)
        game-state
        (recur
          (-> game-state
              (play-card/play-card 0 0 (strategy1 iteration))
              (play-card/play-card 1 0 (strategy2 iteration)))
          (dec iteration)))))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory-conditions/finished? %))
    (create-game/new-game))

  (expect
    #(victory-conditions/finished? %)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] 0))))

(defexpect winning-player
  ; Winner isn't set if game hasn't ended
  (expect
    nil
    (-> (create-game/new-game)
        (victory-conditions/winner)))

  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    #(= (victory-conditions/winner %) 0)
    (play-a-game-helper
      (fn [i] (mod i 4))
      (fn [i] 0)))
  (expect
    #(= (victory-conditions/winner %) 1)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory-conditions/winner %) 2)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] 0))))
