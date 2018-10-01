(ns api.player-view-functions
  (:require [rules.victory-conditions :as victory]
            [configs.messages :as messages]))

(defn ^:private partial-card
  "Returns only certain keys of a card and change the :owner"
  [card wanted-keys owner]
  (assoc (select-keys card wanted-keys)
         :owner owner))

(defn get-cards
  "Returns cards as seen by a player"
  [game-state player-id]
  (vec (map
         #(cond
            (and (and (= (:owner %) player-id)
                      (contains? % :ability))
                      (= (get-in % [:location 0]) :hand))
            (merge (partial-card % [:power :location] "me")
                   ((:ability %)))

            (= (:owner %) player-id)
            (partial-card % [:power :location] "me")

            (= (get-in % [:location 0]) :row)
            (partial-card % [:power :location] "opp")

            :else
            (partial-card % [:location] "opp"))
         (:cards game-state))))

(defn get-rows
  "Return the rows info as seen by the player"
  [game-state player-id]
  (let [player-ids (:player-ids game-state)
        opp-id (if (= (first player-ids) player-id)
                 (second player-ids)
                 (first player-ids))]
    (loop [rows (:rows game-state)
           row 0]
      (if (= row (count (:rows game-state)))
        rows
        (recur
          (assoc-in
            rows
            [row :scores]
            [(victory/points-in-row game-state row player-id)
             (victory/points-in-row game-state row opp-id)])
          (inc row))))))

(defn get-scores
  "Return the scores as seen by the player"
  [game-state player-id]
  (let [player-ids (:player-ids game-state)
        opp-id (if (= (first player-ids) player-id)
                 (second player-ids)
                 (first player-ids))]
    [(victory/get-won-rows game-state player-id)
     (victory/get-won-rows game-state opp-id)]))

(defn get-winner
  "Return the winner as seen by the player"
  [game-state player-id]
  (let [winner (victory/winner game-state)
        player-ids (:player-ids game-state)
        opp-id (if (= (first player-ids) player-id)
                 (second player-ids)
                 (first player-ids))]
    (cond (= winner player-id)
          "me"
          (= winner opp-id)
          "opp"
          :else
          winner)))

(defn get-status
  "Returns the status of the game from a player's perspective"
  [game-state player-id]
  (:status game-state
           (if (nil? (get-in game-state [:next-play (keyword player-id)]))
             messages/play
             messages/wait)))
