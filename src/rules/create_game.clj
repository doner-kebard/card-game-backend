(ns rules.create-game
  (:require [configs.hands :as hands]
            [configs.cards :as cards]
            [configs.rows :as rows]
            [configs.player-ids :as player-ids]))

(defn ^:private locate-in-hand
  "Creates location for a vec of cards on player's hand"
  [hand player]
  (vec (map #(merge (% cards/card-map)
                    {:card-name (name %) :location [:hand] :owner player})
            hand))) 

(defn new-game
  "Creates a new game object"
  ([] (new-game {}))
  ([ini-config]
    (let [player-ids (:player-ids ini-config player-ids/default-player-ids)]
    {
      :player-ids player-ids
      :cards (let [hands (:hands ini-config hands/default-hands)]
               (vec (concat (locate-in-hand (first hands) (first player-ids))
                            (locate-in-hand (second hands) (second player-ids)))))
      :rows (:rows ini-config rows/default-rows)
      :next-play {} 
   })))
