(ns api.player-view-functions
  (:require [rules.victory-conditions :as victory]
            [api.conversions :as conversions]
            [configs.messages :as messages]))

(defn get-hand
  "Return hand as seen by the player"
  [game-state player-id]
  (get-in game-state [:players (conversions/player-num game-state player-id) :hand]))

(defn get-row-cards
  "Returns the cards as seen by the player"
  [row-cards game-state player-id]
  (mapv (fn [card]
          (assoc card :owner
                 (conversions/translate-player game-state (:owner card) player-id)))
        row-cards))

(defn get-rows
  "Return the rows as seen by the player"
  [game-state player-id]
  (mapv (fn [row]
          (get-row-cards (:cards row) game-state player-id))
        (:rows game-state)))

(defn get-rows-power
  "Return the powers of each row as seen by the player"
  [game-state player-id]
  (let [player (conversions/player-num game-state player-id)
        opponent (mod (inc player) 2)]
    (loop [rows-power []
           rows (:rows game-state)]
      (if (empty? rows)
        rows-power
        (recur
          (conj rows-power
                [(victory/points-in-row (:cards (first rows)) player)
                 (victory/points-in-row (:cards (first rows)) opponent)])
          (rest rows))))))

(defn get-scores
  "Return the scores as seen by the player"
  [game-state player-id]
  (let [player (conversions/player-num game-state player-id)
        opponent (mod (inc player) 2)]
    [(victory/get-won-rows game-state player)
     (victory/get-won-rows game-state opponent)]))

(defn get-winner
  "Return the winner as seen by the player"
  [game-state player-id]
  (conversions/translate-player game-state (victory/winner game-state) player-id))

(defn get-status
  "Returns the status of the game from a player's perspective"
  [game-state player-id]
  (cond (= (count (:player-ids game-state)) 1) messages/no-opp
        (nil? (get-in game-state [:next-play (conversions/player-num game-state player-id)])) messages/play
        :else messages/wait))
