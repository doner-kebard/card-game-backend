(ns card-game.core.create-game
  (:require [configs :as configs]))

(defn new-player
  "Creates a new player object"
  ([]
   {
    :hand (configs/ini-hand)
    }))

(defn new-game
  "Creates a new game object"
  ([]
   {
    :players (vec (repeat 2 (new-player)))
    :rows (vec (repeat 5 []))
    :next-play [nil nil]
    }))
