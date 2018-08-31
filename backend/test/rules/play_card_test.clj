(ns rules.play-card-test
  (:require [expectations.clojure.test :refer :all]
            [rules.play-card :as play-card]
            [configs.messages :as messages]))

  (def game-state {:cards [{:power 0 :location [:hand]} {:power 10 :add-power -1} {:power 100 :add-power -100}]
                   :rows [{}{}{}{}]
                   :next-play {:p0 {:card-id 0 :row-id 0} :p1 {:card-id 1 :row-id 3 :target 2}}
                   :player-ids ["p0" "p1"]})

(defexpect move-card

  ; Moves a card to a chosen row
  (expect
    {:my-card "yes" :location [:row 3]}
    (get-in (play-card/move-card {:cards [{}{:my-card "yes"}]} 1 3)
            [:cards 1]))
  
  (expect
    {:location [:row 0]}
    (get-in (play-card/move-card {:cards [{:location "nowhere"}]} 0 0)
            [:cards 0])))

(defexpect apply-add-power

  ; Does nothing when play doesn't have add-power
  (expect
    game-state
    (play-card/apply-add-power game-state {:card-id 0}))
  
  ; Changes power
  (expect
    99
    (get-in (play-card/apply-add-power game-state {:card-id 1 :target 2})
            [:cards 2 :power]))
  
  (expect
    -100
    (get-in (play-card/apply-add-power game-state {:card-id 2 :target 0})
            [:cards 0 :power])))

(defexpect apply-play
  
  ; Moves cards correctly
  (expect
    [:row 1]
    (get-in (play-card/apply-play game-state {:card-id 0 :row-id 1})
            [:cards 0 :location]))
  
  ; Applies abilities correctly
  (expect
    [{:power -100 :location [:hand]} {:power 10 :add-power -1} {:power 100 :add-power -100 :location [:row 1]}]
    (:cards (play-card/apply-play game-state {:card-id 2 :row-id 1 :target 0}))))

(defexpect apply-all-plays
  
  (expect
    {:cards [{:power 0 :location [:row 0]} {:power 10 :add-power -1 :location [:row 3]} {:power 99 :add-power -100}]
     :rows [{}{}{}{}]
     :next-play {}
     :player-ids ["p0" "p1"]}
    (play-card/apply-all-plays game-state)))

(defexpect crowded-row?

  (expect
    false
    (play-card/crowded-row? {:cards [{}]} 0 "p0"))
  
  (expect
    false
    (play-card/crowded-row? {:cards [{:location [:row 1] :owner "p0"}
                                     {:location [:row 1] :owner "p1"}]
                             :rows [{}{:limit 2}]} 1 "p0"))
  (expect
    true
    (play-card/crowded-row? {:cards [{:location [:row 1] :owner "crowder"}
                                     {:location [:row 1] :owner "mr. X"}]
                             :rows [{}{:limit 1}]} 1 "crowder")))

(defexpect play-card
  
  ; Returns errors correctly
  (expect
    {:error messages/out-of-turn}
    (play-card/play-card {:next-play {:doubler {}}} "doubler" 0 0))
  
  (expect
    {:error messages/not-owned-card}
    (play-card/play-card {:next-play {:police {}}
                          :cards [{:owner "police"}]} "thief" 0 0))

  (expect
    {:error messages/no-row}
    (play-card/play-card {:next-play {}
                          :cards [{:owner 0}]
                          :rows [{}{}{}{}]} 0 0 4))

  (expect
    {:error messages/row-limit}
    (play-card/play-card {:next-play {} 
                          :rows [{:limit 0}]
                          :cards [{:owner "p0"}]} "p0" 0 0))
  
  (expect
    {:error messages/need-target}
    (play-card/play-card {:next-play {}
                          :rows [{}]
                          :cards [{:add-power 1 :owner "dumb"}]} "dumb" 0 0))
  
  ; Saves next-play
  (expect
    {:saver {:card-id 0 :row-id 1 :target nil}}
    (:next-play
      (play-card/play-card {:next-play {} 
                            :rows [{}{}{}]
                            :cards [{:owner "saver"}]} "saver" 0 1)))
  
  (expect
    {:p {:card-id 2 :row-id 3 :target 0}}
    (:next-play
      (play-card/play-card {:next-play {}
                            :rows [{}{}{}{}]
                            :cards [{}{}{:owner "p" :add-power 1}]} "p" 2 3 0)))

  (expect
    [{:owner "p"}]
    (:cards (play-card/play-card {:cards [{:owner "p"}]
                                  :rows [{}]
                                  :next-play {}} "p" 0 0)))

  ; Applies a play
  (expect 
    {:cards [{:power 0 :location [:row 0] :owner "p0"} {:power 10 :add-power -1 :owner "p1" :location [:row 3]} {:power 99 :add-power -100}]
     :rows [{}{}{}{}{}]
     :next-play {}}
    (play-card/play-card {:cards [{:power 0 :location [:hand] :owner "p0"} {:power 10 :add-power -1 :owner "p1"} {:power 100 :add-power -100}]
                          :rows [{}{}{}{}{}]
                          :next-play {:p1 {:card-id 1 :row-id 3 :target 2}}} "p0" 0 0)))
