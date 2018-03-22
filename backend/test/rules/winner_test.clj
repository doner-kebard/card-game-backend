(ns rules.winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.victory-conditions :as victory]))

(defexpect finished-game
  ; We can tell if a game is finished
  (expect
    false
    (victory/finished?
      {:players [{:hand [{}]} {:hand [{}]}]}))

  (expect
    false
    (victory/finished?
      {:players [{:hand []} {:hand [{}]}]}))

  (expect
    false
    (victory/finished?
      {:players [{:hand [{} {}]} {:hand []}]}))

  (expect
    true
    (victory/finished?
      {:players [{:hand []} {:hand []}]})))

(defexpect winning-player
  ; Winner isn't set if game hasn't ended
  (expect
    nil
    (victory/winner
      {:players [{:hand [{}]} {:hand []}]}))
       

  ; Winner returns the winning player on a finished game, or 2 on a tie
  (expect
    0
    (victory/winner
      {:players [{:hand []} {:hand[]}]
       :rows [{:cards [{:power 1 :owner 0}]}]}))
  (expect
    1
    (victory/winner
      {:players [{:hand []} {:hand[]}]
       :rows [
              {:cards [{:power 20 :owner 0}]}
              {:cards [{:power 1 :owner 1}]}
              {:cards [{:power 5 :owner 0} {:power 6 :owner 1}]}]}))
  (expect
    2
    (victory/winner
      {:players [{:hand []} {:hand[]}]
       :rows [
              {:cards [{:power 20 :owner 0}]}
              {:cards [{:power 1 :owner 1}]}
              {:cards [{:power 5 :owner 0} {:power 5 :owner 1}]}]})))
