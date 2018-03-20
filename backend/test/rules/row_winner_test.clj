(ns rules.row-winner-test
  (:require [expectations.clojure.test :refer :all]
            [rules.victory-conditions :as victory]))

(defexpect row-winner
  (expect
    0
    (victory/get-won-rows {:rows [[] [] [] [] []]} 0))

  (expect
    1
    (victory/get-won-rows {:rows [[{:power 1 :owner 5}] [] [] [] []]} 5))

  (expect
    4
    (victory/get-won-rows
      {:rows [
              [{:power 1 :owner 0}]
              [{:power 200 :owner 1} {:power 9001 :owner 0}]
              [{:power -1 :owner 0} {:power 5 :owner 1} {:power 9 :owner 0}]
              [{:power 3 :owner 1} {:power 1 :owner 0}
               {:power 2 :owner 0} {:power -1 :owner 1}]
              []]}
      0)))
