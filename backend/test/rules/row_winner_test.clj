(ns rules.winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [autoplay :as autoplay]
            [rules.victory-conditions :as victory]))

(defexpect row-winner
  ; The game begins with players winning 0 rows
  (expect
    0
    (-> (create-game/new-game)
        (victory/get-won-rows 0)))
  
  ; Game counts row wons correctly
  (expect
    1
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 0 1)
        (victory/get-won-rows 0)))
  (expect
    #(= (victory/get-won-rows % 0) 1)
    (autoplay/as-rules
      (fn [i] 4)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory/get-won-rows % 1) 4)
    (autoplay/as-rules
      (fn [i] 4)
      (fn [i] (mod i 4))))
  (expect
    #(= (victory/get-won-rows % 0) 0)
    (autoplay/as-rules
      (fn [i] 0)
      (fn [i] 0))))
