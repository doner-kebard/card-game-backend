(ns rules.winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.victory-conditions :as victory]
            [configs.hands :as hands]
            [autoplay :as autoplay]))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    #(not (victory/finished? %))
    (create-game/new-game))

  (expect
    #(victory/finished? %)
    (autoplay/as-rules
      (fn [i] 0)
      (fn [i] 0))))

(defexpect winning-player
  ; Winner isn't set if game hasn't ended
  (expect
    nil
    (-> (create-game/new-game)
        (victory/winner)))

  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    #(= (victory/winner %) 0)
    (autoplay/as-rules
      (fn [i] (mod i 4))
      (fn [i] 0)))
  (expect
    #(= (victory/winner %) 1)
    (autoplay/as-rules
      (fn [i] 0)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory/winner %) 2)
    (autoplay/as-rules
      (fn [i] 0)
      (fn [i] 0))))
