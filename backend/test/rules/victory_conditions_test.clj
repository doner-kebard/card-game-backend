(ns rules.victory-conditions-test
  (:require [expectations.clojure.test :refer :all]
            [rules.victory-conditions :as victory]))

(def game-state {:cards [{:power 1 :location [:row 0] :owner "p0"}
                         {:power 2 :location [:row 1] :owner "p0"}
                         {:power 4 :location [:row 0] :owner "p0"}
                         {:power 8 :location [:row 1] :owner "p1"}
                         {:power 16 :location [:row 0] :owner "p1"}
                         {:power 32 :location [:hand] :owner "p1"}
                         {:power 64 :location [:row 1] :owner "p1"}
                         {:power 128 :location [:row 2] :owner "p0"}
                         {:power 128 :location [:row 2] :owner "p1"}
                         {:power 256 :location [:row 3] :owner "p0"}]
                 :rows [{}{}{}{}{}]
                 :player-ids ["p0" "p1"]})

(defexpect finished?

  ; Correctly determines finished games
  (expect
    true
    (victory/finished? {:cards []}))
  
  (expect
    true
    (victory/finished? {:cards [{:location [:row 1]}
                                {:location [:row 2]}]}))
  
  (expect
    false
    (victory/finished? {:cards [{:location [:hand]}]}))
  
  (expect
    false
    (victory/finished? game-state)))

(defexpect points-in-row

  ; Correctly counts points
  (expect
    5
    (victory/points-in-row game-state 0 "p0"))
  
  (expect
    2
    (victory/points-in-row game-state 1 "p0"))
  
  (expect
    16
    (victory/points-in-row game-state 0 "p1"))
  
  (expect
    72
    (victory/points-in-row game-state 1 "p1")))

(defexpect player-wins-row?

  ; Correctly determines row-winner
  (expect
    false
    (victory/player-wins-row? game-state 0 "p0"))
  
  (expect
    true
    (victory/player-wins-row? game-state 0 "p1"))
  
  (expect
    true
    (victory/player-wins-row? game-state 3 "p0"))
  
  ; No winner for ties
  (expect
    false
    (victory/player-wins-row? game-state 2 "p0"))

  (expect
    false
    (victory/player-wins-row? game-state 2 "p1")))

(defexpect get-won-rows

  ; Correctly gets won rows
  (expect
    1
    (victory/get-won-rows game-state "p0"))

  (expect
    2
    (victory/get-won-rows game-state "p1")))

(defexpect most-won-rows

  ; Correctly get current-winner
  (expect
    "p1"
    (victory/most-won-rows game-state))
  
  (expect
    "winner"
    (victory/most-won-rows {:cards [{:power 1 :location [:row 0] :owner "winner"}]
                            :rows [{}]
                            :player-ids ["winner" "unknown"]}))
  
  (expect
    ""
    (victory/most-won-rows {:cards [{:power 1 :owner "tier"}]
                            :rows [{}{}]
                            :player-ids ["un-tier" "tier"]})))

(defexpect winner

  ; nil when game's not finished
  (expect
    nil
    (victory/winner game-state))
  
  ; winner when game's finnished
  (expect
    "U"
    (victory/winner {:cards [{:power -1 :location [:row 0] :owner "I"}
                             {:power 0 :location [:row 0] :owner "U"}]
                     :rows [{}]
                     :player-ids ["I" "U"]}))
  
  (expect
    "champ"
    (victory/winner {:cards [{:power 1 :location [:row 1] :owner "champ"}
                             {:power 1 :location [:row 2] :owner "champ"}
                             {:power 1 :location [:row 0] :owner "noob"}]
                     :rows [{}{}{}]
                     :player-ids ["noob" "champ"]}))
  
  (expect
    ""
    (victory/winner {:cards [{}]})))
