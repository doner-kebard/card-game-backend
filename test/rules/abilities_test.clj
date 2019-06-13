(ns rules.abilities-test
  (:require [expectations.clojure.test :refer :all]
            [rules.abilities :as abilities]))

(defexpect required-targets
  (expect
    1
    (abilities/required-targets [[:strengthen 1]]))
  (expect
    1
    (abilities/required-targets [[:weaken 1]]))
  (expect
    0
    (abilities/required-targets [[:row-affinity "" 1]])))

(defexpect strengthen-and-weaken

  ; Adds power
  (expect
    7
    (get-in ((abilities/generate-abilities-fn [[:strengthen 5]]) {:cards [{:power 2}]} {:target 0})
            [:cards 0 :power]))

  ; Removes power
  (expect
    -10
    (get-in ((abilities/generate-abilities-fn [[:weaken 13]]) {:cards [{}{:power 3}]} {:target 1 :card-id 91 :something-else "whatever"})
            [:cards 1 :power])))

(defexpect row-affinity

  ; Does nothing when shouldn't
  (expect
    42
    (get-in ((abilities/generate-abilities-fn [[:row-affinity "invi" 10]]) {:cards [{:power 42}] :rows [{}]} {:row-id 0 :card-id 0})
            [:cards 0 :power]))
  (expect
    99
    (get-in ((abilities/generate-abilities-fn [[:row-affinity "invi" 10]]) {:cards [{}{:power 99}] :rows [{}{:type "type-0"}]} {:row-id 1 :card-id 1})
            [:cards 1 :power]))
  
  ; Adds power when should
  (expect
    52
    (get-in ((abilities/generate-abilities-fn [[:row-affinity "invi" 10]]) {:cards [{:power 42}] :rows [{:type "invi"}]} {:row-id 0 :card-id 0})
            [:cards 0 :power]))

  (expect
    -2
    (get-in ((abilities/generate-abilities-fn [[:row-affinity "vivi" -3]]) {:cards [{:power 42} {:power 1}] :rows [{}{}{:type "vivi"}]} {:row-id 2 :card-id 1})
            [:cards 1 :power])))

(defexpect multi-abilities

  (expect
    11
    (get-in ((abilities/generate-abilities-fn [[:row-affinity "first-row" 10][:row-affinity "second-row" 100]])
             {:cards [{:power 1}] :rows [{:type "first-row"} {:type "second-row"}]}
             {:row-id 0 :card-id 0})
            [:cards 0 :power]))

  (expect
    101
    (get-in ((abilities/generate-abilities-fn [[:row-affinity "first-row" 10][:row-affinity "second-row" 100]])
             {:cards [{:power 1}] :rows [{:type "first-row"} {:type "second-row"}]}
             {:row-id 1 :card-id 0})
            [:cards 0 :power])))
