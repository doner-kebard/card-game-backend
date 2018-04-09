(ns rules.new-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory]
            [configs.hands :as hands]
            [configs.rows :as rows]
            [autoplay :as autoplay]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (create-game/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (create-game/new-game))

  ; Hand's not empty
  (expect
    false
    (-> (create-game/new-game)
        (get-in [:players 0 :hand])
        (empty?)))

  ; Rows begin empty and with limit
  (expect
    [{:limit (nth rows/default-limits 0) :cards []}
     {:limit (nth rows/default-limits 1) :cards []}
     {:limit (nth rows/default-limits 2) :cards []}
     {:limit (nth rows/default-limits 3) :cards []}
     {:limit (nth rows/default-limits 4) :cards []}]
    (-> (create-game/new-game)
        :rows)))

(defexpect starting-hand
  ; Players start with expected hand
  (expect
    hands/default-hand
    (-> (create-game/new-game)
        :players
        first
        :hand))
  (expect
    hands/default-hand
    (-> (create-game/new-game)
        :players
        second
        :hand)))

(defexpect configs.game
  ; Game can start with different configs
  (expect
    [{:hand [{:power 0 :attribute 9001}{:power 1}]}
     {:hand [{:whatever 10}]}]
    (-> (create-game/new-game {:hands [[{:power 0 :attribute 9001}
                                        {:power 1}]
                                       [{:whatever 10}]]})
        :players))
  
  (expect
    [{:limit 0 :cards []} {:limit 2 :cards []} {:limit 4 :cards []} {:limit 6 :cards []} {:limit 8 :cards []}]
    (-> (create-game/new-game {:limits [0 2 4 6 8]})
        :rows)))

(defexpect next-play
  ; Game starts with a nil next-play
  (expect
    [nil nil]
    (-> (create-game/new-game)
        :next-play)))
