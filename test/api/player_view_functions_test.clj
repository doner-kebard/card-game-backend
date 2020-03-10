(ns api.player-view-functions-test
  (:require [expectations.clojure.test :refer :all]
            [api.player-view-functions :as functions]
            [configs.messages :as messages]))

(defexpect get-cards
  
  ; Gives cards as seen by a player
  (expect
    [{:location [:hand] :owner "opp"}
     {:card-name "second" :power -1 :location [:row 0] :owner "opp"}
     {:card-name "third" :power 1 :location [:hand] :owner "me" :target 0}
     {:card-name "fourth" :power 100 :location [:row 1] :owner "me"}]
    (functions/get-cards {:cards [{:card-name "first" :power 99 :location [:hand] :owner "opp_name"}
                                  {:card-name "second" :power -1 :location [:row 0] :owner "opp_name" :secret-attr "who knows?"}
                                  {:card-name "third" :power 1 :internal-attr "kill" :location [:hand] :owner "me_name"}
                                  {:card-name "fourth" :power 100 :location [:row 1] :owner "me_name"}]}
                        "me_name"))

  ; Correctly returns cards with abilities
  (expect
    [{:card-name "c1" :power 1 :location [:hand] :owner "me" :abilities [[:strengthen 100]] :target 1}
     {:card-name "c2" :power 17 :location [:hand] :owner "me" :abilities [[:weaken 1]] :target 1}
     {:location [:hand] :owner "opp"}
     {:card-name "c4" :power -20 :location [:row 0] :owner "me"}
     {:card-name "c5" :power 999 :location [:row 1] :owner "opp"}]
    (functions/get-cards {:cards [{:card-name "c1" :power 1 :location [:hand] :owner "I" :abilities [[:strengthen 100]]}
                                  {:card-name "c2" :power 17 :location [:hand] :owner "I" :abilities [[:weaken 1]]}
                                  {:card-name "c3" :power 17 :location [:hand] :owner "U" :abilities [[:weaken 1]]}
                                  {:card-name "c4" :power -20 :location [:row 0] :owner "I" :abilities [[:strengthen 76]]}
                                  {:card-name "c5" :power 999 :location [:row 1] :owner "U" :abilities [[:strengthen 22]]}]}
                        "I")))

(defexpect get-rows

  ; Gives rows with scores
  (expect
    [{:limit 1 :scores [0 0]}
     {:limit 4 :type "annoyer" :scores [1 2]}
     {:scores [12 0]}]
    (functions/get-rows {:cards [{:power 1 :location [:row 1] :owner "I"}
                                 {:power 2 :location [:row 1] :owner "U"}
                                 {:power 4 :location [:row 2] :owner "I"}
                                 {:power 8 :location [:row 2] :owner "I"}]
                         :rows [{:limit 1}{:limit 4 :type "annoyer"}{}]
                         :player-ids ["I" "U"]}
                        "I")))

(defexpect get-scores

  ; Gives global scores
  (expect
    [2 1]
    (functions/get-scores {:cards [{:power 10 :location [:row 0] :owner "jo"}
                                   {:power 1 :location [:row 0] :owner "tu"}
                                   {:power 3 :location [:row 1] :owner "jo"}
                                   {:power 4 :location [:row 1] :owner "jo"}
                                   {:power 5 :location [:row 1] :owner "tu"}
                                   {:power 9 :location [:row 2] :owner "tu"}]
                           :rows [{}{}{}{}]
                           :player-ids ["tu" "jo"]}
                          "jo")))

(defexpect get-winner

  ; No winner
  (expect
    nil
    (functions/get-winner {:cards [{:location [:hand]}]
                           :player-ids ["john" "jihn"]} "john"))

  ; Tie
  (expect
    ""
    (functions/get-winner {:cards [{}] :rows[{}{}]
                           :player-ids ["jihn" "john"]} "john"))

  ; Gets winner
  (expect
    "me"
    (functions/get-winner {:cards [{:power 1 :location [:row 1] :owner "winner"}]
                           :rows [{}{}]
                           :player-ids ["winner" "someone"]}
                          "winner"))
  (expect
    "opp"
    (functions/get-winner {:cards [{:power 1 :location [:row 1] :owner "winner"}]
                           :rows [{}{}]
                           :player-ids ["winner" "someone"]}
                          "someone")))

(defexpect get-game-status
  
  ; Status are as intended
  (expect
    messages/play
    (functions/get-game-status {:player-ids ["p1" "player"] :next-play {}} "player"))
  
  (expect
    messages/play
    (functions/get-game-status {:player-ids ["jugador1" "slow"] :next-play {:quick {}}} "slow"))
  
  (expect
    messages/wait
    (functions/get-game-status {:player-ids ["waiter" "12345"] :next-play {:waiter {}}} "waiter"))
  
  (expect
    messages/no-opp
    (functions/get-game-status {:player-ids ["lonelyplayer"] :next-play {}} "lonelyplayer")))
