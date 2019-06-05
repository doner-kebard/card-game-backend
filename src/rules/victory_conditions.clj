(ns rules.victory-conditions
  (:require [rules.count-cards :as count-cards]))

(defn finished?
  "Tells us if a game has finished"
  [game-state]
  (zero? (count-cards/count-cards game-state {:location [:hand]})))

(defn points-in-row
  "Tells us how many points the cards of a row has for a certain player"
  [game-state row-id player-id]
  (count-cards/count-cards game-state
                           {:location [:row row-id]
                            :owner player-id}
                           :power))

(defn player-wins-row?
  "Tells us if a player is winning a row"
  [game-state row-id player-id]
  (let [opponent-id (first (filter
                             #(not= % player-id)
                             (:player-ids game-state)))]
    (> (points-in-row game-state row-id player-id)
       (points-in-row game-state row-id opponent-id))))

(defn get-won-rows
  "Tells us how many rows a player is winning"
  [game-state player-id]
  (count
     (filter #(player-wins-row? game-state % player-id)
             (range 0 (count (:rows game-state))))))

(defn most-won-rows
  "Which player is winning the most rows?"
  [game-state]
  (let [player-ids (:player-ids game-state)
        won-rows (map #(get-won-rows game-state %) player-ids)]
    (cond
      (nil? player-ids) nil
      (< (count player-ids) 2) nil
      (< (first won-rows) (second won-rows)) (second player-ids)
      (> (first won-rows) (second won-rows)) (first player-ids)
      :else "")))
    
(defn winner
  "Tells us if there's a winner and if so, who it is"
  [game-state]
  (if (finished? game-state)
    (most-won-rows game-state)
    nil))
