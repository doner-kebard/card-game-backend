(ns rules.alter-card-test
  (:require [expectations.clojure.test :refer :all]
            [rules.alter-card :as alter-card]))

(defexpect alter-card
  
  ; Can change value of a card
  (expect
    9000
    (get-in (alter-card/alter-card {:cards [{}]} 0 {:coolness 9000})
            [:cards 0 :coolness]))
  
  (expect
    "potatos"
    (get-in (alter-card/alter-card {:cards [{}{:food "tomatos"}]} 1 {:food "potatos"})
            [:cards 1 :food])))

(defexpect add-power

  ; Adds power
  (expect
    7
    (get-in (alter-card/add-power {:cards [{:power 2}]} 0 5)
            [:cards 0 :power]))

  ; Removes power
  (expect
    -10
    (get-in (alter-card/add-power {:cards [{}{:power 3}]} 1 -13)
            [:cards 1 :power])))

