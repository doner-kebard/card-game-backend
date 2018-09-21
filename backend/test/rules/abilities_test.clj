(ns rules.abilities-test
  (:require [expectations.clojure.test :refer :all]
            [rules.abilities :as ability]))

(defexpect add-power

  ; Adds power
  (expect
    7
    (get-in ((ability/add-power 5) {:cards [{:power 2}]} 0)
            [:cards 0 :power]))

  ; Removes power
  (expect
    -10
    (get-in ((ability/add-power -13) {:cards [{}{:power 3}]} 1)
            [:cards 1 :power])))

