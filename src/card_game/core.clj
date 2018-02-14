(ns card-game.core
  (:use card-game.build-card)
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn new-player
  "Creates a new player object"
  ([] (new-player 12))
  ([cards]
   {
    :hand (vec (repeat cards (build-card)))
    }))

(defn new-game
  "Creates a new game object"
  ([] (new-game 12))
  ([cards]
   {
    :players (vec (repeat 2 (new-player cards)))
    :rows (vec (repeat 5 []))
    }))

(defn add-card-to-row
  "Adds a card onto the specified row"
  [game-state card row]
  (update-in game-state [:rows row] #(conj % card)))

(defn modify-hand
  "Modifies a player's hand according to some function"
  [game-state player modification]
  (update-in game-state [:players player :hand] modification))

(defn play-card
  "Plays a card from hand onto a game row"
  [game-state player index row]
  (let [card (get-in game-state [:players player :hand index])]
    (-> game-state
        (add-card-to-row (assoc card :owner player) row)
        (modify-hand player #(vec (concat
                                    (subvec % 0 index)
                                    (subvec % (inc index)))))
        ))
  )

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state player index new-values]
  (-> game-state
      (modify-hand player #(assoc % index (merge (get % index) new-values)))
      )
  )

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
