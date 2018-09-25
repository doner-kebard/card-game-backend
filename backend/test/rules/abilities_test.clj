(ns rules.abilities-test
  (:require [expectations.clojure.test :refer :all]
            [rules.abilities :as ability]))

(defexpect add-power

  ; Adds power
  (expect
    7
    (get-in ((ability/add-power 5) {:cards [{:power 2}]} {:target 0})
            [:cards 0 :power]))

  ; Removes power
  (expect
    -10
    (get-in ((ability/add-power -13) {:cards [{}{:power 3}]} {:target 1 :card-id 91 :something-else "whatever"})
            [:cards 1 :power])))

(defexpect type-add-power

  ; Does nothing when shouldn't
  (expect
    42
    (get-in ((ability/type-add-power "invi" 10) {:cards [{:power 42}] :rows [{}]} {:row-id 0 :card-id 0})
            [:cards 0 :power]))
  (expect
    99
    (get-in ((ability/type-add-power "invi" 10) {:cards [{}{:power 99}] :rows [{}{:type "type-0"}]} {:row-id 1 :card-id 1})
            [:cards 1 :power]))
  
  ; Adds power when should
  (expect
    52
    (get-in ((ability/type-add-power "invi" 10) {:cards [{:power 42}] :rows [{:type "invi"}]} {:row-id 0 :card-id 0})
            [:cards 0 :power]))

  (expect
    -2
    (get-in ((ability/type-add-power "vivi" -3) {:cards [{:power 42} {:power 1}] :rows [{}{}{:type "vivi"}]} {:row-id 2 :card-id 1})
            [:cards 1 :power])))
