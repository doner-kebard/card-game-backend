(ns rules.winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory-conditions]))

(defexpect row-winner
  ; The game begins with players winning 0 rows
  (expect
    0
    (-> (create-game/new-game)
        (victory-conditions/get-won-rows 0)))
  
  ; Game counts row wons correctly
  (expect
    1
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 0 1)
        (victory-conditions/get-won-rows 0)))
  (expect
    #(= (victory-conditions/get-won-rows % 0) 1)
    (play-a-game-helper
      (fn [i] 4)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory-conditions/get-won-rows % 1) 4)
    (play-a-game-helper
      (fn [i] 4)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory-conditions/get-won-rows % 0) 0)
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] 0))))
