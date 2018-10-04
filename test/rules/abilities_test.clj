(ns rules.abilities-test
  (:require [expectations.clojure.test :refer :all]
            [rules.abilities :as ability]))

(defexpect add-power

  ; Adds power
  (expect
    7
    (get-in ((:ability (ability/generate-ability-fn [:add-power 5])) {:cards [{:power 2}]} {:target 0})
            [:cards 0 :power]))

  ; Removes power
  (expect
    -10
    (get-in ((:ability (ability/add-power -13)) {:cards [{}{:power 3}]} {:target 1 :card-id 91 :something-else "whatever"})
            [:cards 1 :power]))
  
  ; Returns info
  (expect
    "Enhance 99"
    (:description (ability/add-power 99)))

  (expect
    "Weaken 1"
    (:description (ability/add-power -1)))
  
  (expect
    1
    (:target (ability/add-power 0))))

(defexpect add-power-on-row-type

  ; Does nothing when shouldn't
  (expect
    42
    (get-in ((:ability (ability/add-power-on-row-type "invi" 10)) {:cards [{:power 42}] :rows [{}]} {:row-id 0 :card-id 0})
            [:cards 0 :power]))
  (expect
    99
    (get-in ((:ability (ability/add-power-on-row-type "invi" 10)) {:cards [{}{:power 99}] :rows [{}{:type "type-0"}]} {:row-id 1 :card-id 1})
            [:cards 1 :power]))
  
  ; Adds power when should
  (expect
    52
    (get-in ((:ability (ability/add-power-on-row-type "invi" 10)) {:cards [{:power 42}] :rows [{:type "invi"}]} {:row-id 0 :card-id 0})
            [:cards 0 :power]))

  (expect
    -2
    (get-in ((:ability (ability/add-power-on-row-type "vivi" -3)) {:cards [{:power 42} {:power 1}] :rows [{}{}{:type "vivi"}]} {:row-id 2 :card-id 1})
            [:cards 1 :power]))
  
  ; Returns info
  (expect
    "Enhance 7 on iris"
    (:description (ability/add-power-on-row-type "iris" 7)))

  (expect
    "Weaken 3 on negat"
    (:description (ability/add-power-on-row-type "negat" -3)))

  (expect
    (not (contains? (ability/add-power-on-row-type "notarget" 1) :target))))
