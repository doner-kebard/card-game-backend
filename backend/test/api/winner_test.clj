(ns api.winner-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [autoplay :as autoplay]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect tie
  ; Both stack all cards on one row -> tie
  (expect
    #(= :tie (:winner %))
    (autoplay/as-api
      (fn [i] 0)
      (fn [i] 0))))

(defexpect i-win
  ; Opponent stacks all cards on one row and I spread -> I win
  (expect
    #(= :me (:winner %))
    (autoplay/as-api
      (fn [i] (mod i 4))
      (fn [i] 0))))

(defexpect i-lose
  ; I stack all cards on one row and opponent spreads -> Opponent wins
  (expect
    #(= :opponent (:winner %))
    (autoplay/as-api
      (fn [i] 0)
      (fn [i] (mod i 4)))))
