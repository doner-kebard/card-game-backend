(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn new-player
  "Creates a new player object"
  [hand]
  {
   :hand hand
  }) 

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
   {

    :players (let [hands (:hands ini-config hands/default-hands)]
               [(new-player (first hands))
                (new-player (second hands))])

    :rows (vec (reduce
                 #(concat %1 [{:limit %2 :cards []}])
                 []
                 (:limits ini-config rows/default-limits)))
    :next-play [nil nil]
   }))
