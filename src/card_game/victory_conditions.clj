(ns card-game.victory-conditions
  (:gen-class))

(defn finished?
  "Tells us if a game has finished"
  [game-state]
  (and
    (= 0 (count (get-in game-state [:players 0 :hand])))
    (= 0 (count (get-in game-state [:players 1 :hand])))))

(defn points-in-row
  "Tells us how many points a certain row contains for a certain player"
  [game-state row player]
  (let [row-data (get-in game-state [:rows row])]
    (reduce
      #(if (= (:owner %2) player)
         (+ %1 (:power %2))
         %1)
      0
      row-data)))

(defn get-points
  "Tells us how many points each player has on each row"
  [game-state]
  (loop [row 0
         points []]
    (if (> row 4)
      points
      (recur
        (inc row)
        (conj 
          points 
          [(points-in-row game-state row 0)
           (points-in-row game-state row 1)])))))

(defn most-points
  "Which player has the most points?"
  [point-list]
  (loop [one 0
         two 0
         points point-list]
    (if (empty? points)
      (cond
        (> one two) 0
        (< one two) 1
        :else 2)
      (let [current (first points)]
        (cond
          (> (first current) (second current)) (recur (inc one) two (rest points))
          (< (first current) (second current)) (recur one (inc two) (rest points))
          :else (recur one two (rest points)))))))

(defn winner
  "Tells us if there's a winner and if so, who it is"
  [game-state]
  (if (finished? game-state)
    (most-points (get-points game-state))
    nil))
