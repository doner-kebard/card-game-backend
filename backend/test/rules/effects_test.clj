(ns rules.effects-test
  (:require [expectations.clojure.test :refer :all]
            [rules.alter-card :as alter-card]))

(defexpect card-altering
  (expect
    [{:power 9001}]
    (alter-card/alter-card [{:power 2}] [0] {:power 9001}))

  (expect
    [{:kiwis "good"} {:porcupines "bad" :with-salt "ok"}]
    (alter-card/alter-card
      [{:kiwis "good"} {:porcupines "bad"}]
      [1]
      {:with-salt "ok"})))

(defexpect add-power
  ; Adds power
  (expect
    {:cards [{:power 30}]}
    (alter-card/add-power {:cards [{:power 10}]} [:cards 0] 20))
  ; Subtracts, does not alter other fields
  (expect
    {:cards [{:power -47 :potatoes "amazing"}]}
    (alter-card/add-power
      {:cards [{:power 10 :potatoes "amazing"}]}
      [:cards 0]
      -57))
  ; Weird path
  (expect
    [nil {:croissant "chocolate" :enchilada {:power 56 :salsa "mild"}}]
    (alter-card/add-power
      [nil {:croissant "chocolate" :enchilada {:power 1 :salsa "mild"}}]
      [1 :enchilada]
      55)))
