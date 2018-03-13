(ns rules.create-game
  (:require [configs.hands :as hands]))

(defn ^:private new-player
  "Creates a new player object"
  ([hand]
   {
    :hand hand
    }))

(defn new-game
  "Creates a new game object"
  ([]
   (new-game {}))
  ([ini-config]
   {
    :players (vec (repeat 2 (new-player (if (contains? ini-config :hand)
                                            (:hand ini-config)
                                            hands/default-hand))))
    :rows (vec (repeat 5 []))
    :next-play [nil nil]
    }))
