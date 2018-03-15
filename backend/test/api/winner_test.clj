(ns api.winner-test
  (:require [expectations.clojure.test :refer :all]
            [autoplay :as autoplay]))

(defexpect win-conditions
  ; Both stack all cards on one row -> tie
  (expect
    #(= :tie (:winner %))
    (autoplay/as-api
      (fn [i] 0)
      (fn [i] 0)))

  ; Opponent stacks all cards on one row and I spread -> I win
  (expect
    #(= :me (:winner %))
    (autoplay/as-api
      (fn [i] (mod i 4))
      (fn [i] 0)))

  ; I stack all cards on one row and opponent spreads -> Opponent wins
  (expect
    #(= :opponent (:winner %))
    (autoplay/as-api
      (fn [i] 0)
      (fn [i] (mod i 4)))))
