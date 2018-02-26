(ns card-game.core
  (:require [card-game.build-card :as build-card])
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
    :hand (vec (repeat cards (build-card/build-card)))
    }))

(defn new-game
  "Creates a new game object"
  ([] (new-game 12))
  ([cards]
   {
    :players (vec (repeat 2 (new-player cards)))
    :rows (vec (repeat 5 []))
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
        (apply-play-card (get-in game-state [:next-play 1]))
        (assoc :next-play [nil nil])))

(defn play-card
  "Takes a playing of a card from hand onto a game row and makes it wait until both players had played"
  [game-state player index row]
  ; Uses stored :next-play to know who is supposed to play
  (if (nil? (get-in game-state [:next-play player]))
      (if (every? nil? (:next-play game-state))
          (assoc-in game-state [:next-play player] {:player player :index index :row row})
          (-> game-state
              (assoc-in [:next-play player] {:player player :index index :row row})
              (apply-all-plays)))
      {:error "Out of turn play"}))

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state path new-values]
  (update-in game-state path #(merge % new-values)))
