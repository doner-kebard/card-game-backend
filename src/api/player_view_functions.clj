(ns api.player-view-functions
  (:require [rules.victory-conditions :as victory]
            [rules.abilities :as abilities]
            [configs.messages :as messages]))

(defn ^:private select-and-merge
  "Select some keys from a map and merges it with another one"
  [my-map wanted-keys merging-map]
  (merge (select-keys my-map wanted-keys)
         merging-map))

(defn get-cards
  "Returns cards as seen by a player"
  [game-state player-id]
  (vec (map
         #(cond
            (and (= (:owner %) player-id)
                 (= (get-in % [:location 0]) :hand))
            (select-and-merge % [:power :location :card-name :abilities]
                            {:owner "me"
                             :target (abilities/required-targets (:abilities % [nil]))})

            (= (:owner %) player-id)
            (select-and-merge % [:power :location :card-name]
                            {:owner "me"})

            (= (get-in % [:location 0]) :row)
            (select-and-merge % [:power :location :card-name]
                            {:owner "opp"})

            :else
            (select-and-merge % [:location] 
                            {:owner "opp"}))
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

(defn get-game-status
  "Returns the status of the game from a player's perspective"
  [game-state player-id]
  (:game-status game-state
           (cond
             (= 1 (count (:player-ids game-state)))
             messages/no-opp
             (nil? (get-in game-state [:next-play (keyword player-id)]))
             messages/play
             :else
             messages/wait)))
