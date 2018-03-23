(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn ^:private new-player
  "Creates a new player object"
  ([hand]
   {
    :hand hand
    }))

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
   {
    :players (vec (repeat 2 (new-player (:hand ini-config hands/default-hand))))
    :rows (vec (reduce
                 #(concat %1 [{:limit %2 :cards []}])
                 []
                 (:limits ini-config rows/default-limits)))
    :next-play [nil nil]
    }))
