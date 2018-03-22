(ns rules.victory-conditions)

(defn finished?
  "Tells us if a game has finished"
  [game-state]
  (and
    (= 0 (count (get-in game-state [:players 0 :hand])))
    (= 0 (count (get-in game-state [:players 1 :hand])))))

(defn points-in-row
  "Tells us how many points the cards of a row has for a certain player"
  [row-cards player]
  (reduce
    #(if (= (:owner %2) player)
       (+ %1 (:power %2))
       %1)
    0
    row-cards))

(defn ^:private player-wins-row?
  "Tells us if a player is winning a row from its data"
  [row-data player]
  (let [opponent (mod (inc player) 2)]
    (> (points-in-row (:cards row-data) player)
       (points-in-row (:cards row-data) opponent))))

(defn get-won-rows
  "Tells us how many rows a player is winning"
  [game-state player]
  (reduce
    #(if (player-wins-row? %2 player) (inc %1) %1)
    0
    (get-in game-state [:rows])))

(defn ^:private most-points
  "Which player has the most points?"
  [game-state]
  (let [one (get-won-rows game-state 0)
        two (get-won-rows game-state 1)]
    (cond
      (> one two) 0
      (< one two) 1
      :else 2)))

(defn winner
  "Tells us if there's a winner and if so, who it is"
  [game-state]
  (if (finished? game-state)
    (most-points game-state)
    nil))
