(ns rules.play-card
  (:require [configs.messages :as messages]))

(defn ^:private add-card-to-row
  "Adds a card onto the specified row"
  [game-state card row]
  (update-in game-state [:rows row] #(conj % card)))

(defn ^:private modify-hand
  "Modifies a player's hand according to some function"
  [game-state player modification]
  (update-in game-state [:players player :hand] modification))

(defn ^:private item-remover
  "Returns a function that removes given index from a vector"
  [index]
  #(vec (concat
          (subvec % 0 index)
          (subvec % (inc index)))))

(defn ^:private remove-card
  "Remove a card from hand"
  [game-state player index]
  (modify-hand game-state player (item-remover index)))

(defn ^:private apply-play-card
  "Plays a card waiting to be played onto the board"
  [game-state play]
    (let [player (:player play)
          index (:index play)
          row (:row play)
          card (get-in game-state [:players player :hand index])]
      (-> game-state
          (add-card-to-row (assoc card :owner player) row)
          (remove-card player index))))

(defn ^:private apply-all-plays
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
      {:error messages/out-of-turn}))
