(ns rules.create-game
  (:require [configs.hand :as hand]))

(defn new-player
  "Creates a new player object"
  ([]
   {
    :hand (hand/ini-hand)
    }))

(defn new-game
  "Creates a new game object"
  ([]
   {
    :players (vec (repeat 2 (new-player)))
    :rows (vec (repeat 5 []))
    :next-play [nil nil]
    }))
