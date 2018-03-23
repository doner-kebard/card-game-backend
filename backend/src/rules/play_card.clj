(ns rules.play-card
  (:require [configs.messages :as messages]))

(defn ^:private add-card-to-row
  "Adds a card onto the specified row"
  [game-state card row-id]
  (update-in game-state [:rows row-id :cards] #(conj % card)))

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
    (let [player-id (:player play)
          index (:index play)
          row-id (:row play)
          card (get-in game-state [:players player-id :hand index])]
      (-> game-state
          (add-card-to-row (assoc card :owner player-id) row-id)
          (remove-card player-id index))))

(defn ^:private apply-all-plays
  "Plays all cards waiting to be played"
  [game-state]
    (-> game-state
        (apply-play-card (get-in game-state [:next-play 0]))
        (apply-play-card (get-in game-state [:next-play 1]))
        (assoc :next-play [nil nil])))

(defn ^:private count-owned-cards
  "Returns number of cards owned by a player in row-cards"
  [row-cards player-id]
  (reduce #(if (= (:owner %2) player-id)
               (inc %1)
               %1)
          0
          row-cards))

(defn ^:private crowded-row?
  "True if a row has :limit cards for player-id"
  [row-data player-id]
  (<= (:limit row-data 9000)
      (count-owned-cards (:cards row-data) player-id)))

(defn play-card
  "Takes a playing of a card from hand onto a game row and makes it wait until both players had played"
  [game-state player-id index row-id & target]
  ; Uses stored :next-play to know who is supposed to play
  (cond (some? (get-in game-state [:next-play player-id]))
        {:error messages/out-of-turn}
        (crowded-row? (get-in game-state [:rows row-id]) player-id)
        {:error messages/row-limit}
        :else
        (if (every? nil? (:next-play game-state))
            (assoc-in game-state [:next-play player-id] {:player player-id :index index :row row-id :target (first target)})
            (-> game-state
                (assoc-in [:next-play player-id] {:player player-id :index index :row row-id :target (first target)})
                (apply-all-plays)))))
