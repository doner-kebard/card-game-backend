(ns rules.effects-test
  (:require [expectations.clojure.test :refer :all]
            [rules.alter-card :as alter-card]
            [rules.play-card :as play-card]))

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

(defexpect playing-add-power
  ; Cards that should add power do so
  (expect
    42
    (-> (play-card/apply-play-card {:rows [{:cards [{:power 24}]}]
                                    :players [{:hand [{:add-power 18}]}]}
                                   {:player 0 :index 0 :row 0 :target [:rows 0 :cards 0]})
        (get-in [:rows 0 :cards 0 :power])))
  (expect
    {:power -2 :coolness 100}
    (-> (play-card/apply-play-card {:rows [{}{:cards [{:power 4 :coolness 100}]}]
                                    :players [{:hand [{:add-power -6}]}]}
                                   {:player 0 :index 0 :row 0 :target [:rows 1 :cards 0]})
       (get-in [:rows 1 :cards 0])))) 


