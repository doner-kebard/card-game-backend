(ns rules.create-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [configs.player-ids :as player-ids]
            [configs.hands :as hands]
            [configs.rows :as rows]))
  
(defexpect locate-in-hand

  ; Cards recieve the correct location
  (expect 
    [{:attr 0 :location [:hand] :owner "-#$-"} {:location [:hand] :owner "-#$-"}]
    (create-game/locate-in-hand [{:attr 0} {}] "-#$-"))
  
  (expect
    [{:power -1 :add-power 3 :location [:hand] :owner "fitipaldi"} {:power 9 :location [:hand] :owner "fitipaldi"}]
    (create-game/locate-in-hand [{:power -1 :add-power 3} {:power 9}] "fitipaldi")))

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
    [{:p 0 :location [:hand] :owner (first player-ids/default-player-ids)}
     {:attr 12 :sometext "" :location [:hand] :owner (second player-ids/default-player-ids)}]
    (:cards (create-game/new-game {:hands [[{:p 0}][{:attr 12 :sometext ""}]]})))
  
  (expect
    [{:limit 92} {} {:type "yabidi"}]
    (:rows (create-game/new-game {:rows [{:limit 92} {} {:type "yabidi"}]})))
  
  ; Game uses default config
  (expect
    (concat hands/default-hand hands/default-hand)
    (map #(apply dissoc % [:location :owner]) (:cards (create-game/new-game))))
  
  (expect
    rows/default-rows
    (:rows (create-game/new-game)))
  
  (expect
    player-ids/default-player-ids
    (:player-ids (create-game/new-game))))
