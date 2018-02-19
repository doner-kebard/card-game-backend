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

(defn item-remover
  "Returns a function that removes given index from a vector"
  [index]
  #(vec (concat
          (subvec % 0 index)
          (subvec % (inc index)))))

(defn remove-card
  "Remove a card from hand"
  [game-state player index]
  (modify-hand game-state player (item-remover index)))

(defn play-card
  "Plays a card from hand onto a game row"
  [game-state player index row]
  (let [card (get-in game-state [:players player :hand index])]
    (-> game-state
        (add-card-to-row (assoc card :owner player) row)
        (remove-card player index))))

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state path new-values]
  (update-in game-state path #(merge % new-values)))
