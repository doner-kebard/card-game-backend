(ns rules.create-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [configs.player-ids :as player-ids]
            [configs.hands :as hands]
            [configs.rows :as rows]))
  
(defexpect new-game
  
  ; Game can be created
  (expect
    #(some? %)
    (create-game/new-game))  

  ; Game has cards
  (expect
    #(not (empty? %))
    (:cards (create-game/new-game)))
  
  ; Game has rows
  (expect
    #(not (empty? %))
    (:rows (create-game/new-game)))

  ; Game has empty next-play
  (expect
    {}
    (:next-play (create-game/new-game)))
  
  ; Game uses config
  (expect
    ["chip" "chop"]
    (:player-ids (create-game/new-game {:player-ids ["chip" "chop"]})))
  
  (expect
    [{:power 8 :card-name "8" :location [:hand] :owner (first player-ids/default-player-ids)}
     {:power 7 :card-name "7" :location [:hand] :owner (second player-ids/default-player-ids)}]
    (:cards (create-game/new-game {:hands [[:8][:7]]})))
  
  (expect
    [{:limit 92} {} {:type "yabidi"}]
    (:rows (create-game/new-game {:rows [{:limit 92} {} {:type "yabidi"}]})))
  
  ; Game uses default config
  (expect
    (map name (concat hands/default-hand hands/default-hand))
    (map :card-name (:cards (create-game/new-game))))
  
  (expect
    rows/default-rows
    (:rows (create-game/new-game)))
  
  (expect
    player-ids/default-player-ids
    (:player-ids (create-game/new-game))))
