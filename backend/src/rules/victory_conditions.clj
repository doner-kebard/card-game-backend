(ns rules.victory-conditions
  (:require [rules.count-cards :as count-cards]))

(defn finished?
  "Tells us if a game has finished"
  [game-state]
  (= 0 (count-cards/count-cards game-state {:location [:hand]})))

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
  (let [opponent-id (if (= player-id (first (:player-ids game-state)))
                      (second (:player-ids game-state))
                      (first (:player-ids game-state)))]
    (> (points-in-row game-state row-id player-id)
       (points-in-row game-state row-id opponent-id))))

(defn get-won-rows
  "Tells us how many rows a player is winning"
  [game-state player-id]
  (loop [won 0 row 0]
    (if (>= row (count (:rows game-state)))
      won
      (recur
        (if (player-wins-row? game-state row player-id)
          (inc won)
          won)
        (inc row)))))

(defn most-won-rows
  "Which player is winning the most rows?"
  [game-state]
  (let [player-ids (:player-ids game-state)
        won-rows (map #(get-won-rows game-state %) player-ids)]
    (loop [winner ""
           most-wons 0
           player-ids player-ids]
      (if (empty? player-ids)
        winner
        (let [tmp-wons (get-won-rows game-state (first player-ids))]
          (cond (= tmp-wons most-wons)
                (recur "" most-wons (rest player-ids))
                (> tmp-wons most-wons)
                (recur (first player-ids) tmp-wons (rest player-ids))
                :else
                (recur winner most-wons (rest player-ids))))))))

(defn winner
  "Tells us if there's a winner and if so, who it is"
  [game-state]
  (if (finished? game-state)
    (most-won-rows game-state)
    nil))
