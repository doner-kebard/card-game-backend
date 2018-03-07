(ns api.helper
  (:require [rules.victory-conditions :as victory-conditions]
            [configs.messages :as messages]))

(defn player-num
  "Translates a Player id into an internal player representation"
  [game-state id] (.indexOf (:player-ids game-state) id))

(defn ^:private translate-player
  "Translates an internal player to a human-readable representation"
  [game-state internal me]
  (cond
    (nil? internal) nil
    (>= internal 2) :tie
    (= me (get-in game-state [:player-ids internal])) :me
    :else :opponent))

(defn get-game-as-player
  "Returns the part of the game-state that ought to be visible to the player"
  [game-state player-id]
  {:game-id (:game-id game-state)
   :player-id player-id
   :hand (get-in game-state [:players (player-num game-state player-id) :hand])
   :rows (mapv #(mapv (fn [card]
                        (assoc card :owner
                          (translate-player game-state (:owner card) player-id)))
                      %)
               (:rows game-state))
   :winner (translate-player game-state (victory-conditions/winner game-state) player-id)})

(defn define-status
  "Returns the status of the game from a player's perspective"
  [game-state player-id]
  (cond (= (count (:player-ids game-state)) 1) messages/no-opp
        (nil? (get-in game-state [:next-play (player-num game-state player-id)])) messages/play
        :else messages/wait))

(defn ^:private uuid [] (str (java.util.UUID/randomUUID)))

(defn create-player
  [game]
  (loop [id (uuid)]
    (if (some #{id} (:player-ids game))
      (recur (uuid))
      (update game :player-ids #(vec (conj % id))))))
