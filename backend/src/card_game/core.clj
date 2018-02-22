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
    :play-wanted [true true]
    :next-play [nil nil]
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

(defn update-play-wanted
  "Change which players are expected to play"
  [game-state player]
  (assoc game-state :play-wanted [(or (not= player 0) (not (get-in game-state [:play-wanted 1]))) 
                                  (or (not= player 1) (not (get-in game-state [:play-wanted 0])))]))

(defn apply-play-card
  "Plays a card waiting to be played onto the board"
  [game-state play]
    (let [player (:player play)
          index (:index play)
          row (:row play)
          card (get-in game-state [:players player :hand index])]
      (-> game-state
          (add-card-to-row (assoc card :owner player) row)
          (remove-card player index))))

(defn apply-all-plays
  "Plays all cards waiting to be played"
  [game-state]
    (-> game-state
        (apply-play-card (get-in game-state [:next-play 0]))
        (apply-play-card (get-in game-state [:next-play 1]))))

(defn play-card
  "Takes a playing of a card from hand onto a game row and makes it wait until both players had played"
  [game-state player index row]
  (if (get-in game-state [:play-wanted player])
      (let [game-state (update-play-wanted game-state player)]
           (if (every? {true true} (:play-wanted game-state))
               (-> game-state
                   (assoc-in [:next-play 1] {:player player :index index :row row})
                   (apply-all-plays))
               (assoc-in game-state [:next-play 0] {:player player :index index :row row})))
      {:error "Out of turn play"}))

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state path new-values]
  (update-in game-state path #(merge % new-values)))
