(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.rows :as rows]))

(defn new-player
  "Creates a new player object"
[hand player]
{
 :hand (mapv (fn [card id] (assoc card :id id))
             hand
             (iterate inc (* player 1000)))})

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
   {

    :players (let [hands (:hands ini-config hands/default-hands)]
               [(new-player (first hands) 0)
                (new-player (second hands) 1)])

    :rows (vec (reduce
                 #(concat %1 [{:limit %2 :cards []}])
                 []
                 (:limits ini-config rows/default-limits)))
    :next-play [nil nil]
   }))
