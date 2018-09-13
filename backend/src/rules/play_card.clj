(ns rules.play-card
  (:require [configs.messages :as messages]
            [rules.alter-card :as alter-card]
            [rules.count-cards :as count-cards]))

(defn move-card
  "Moves a card to the specified row"
  [game-state card-id row-id]
  (assoc-in game-state [:cards card-id :location] [:row row-id]))

(defn apply-add-power
  "Applies an add-power ability"
  [game-state play]
  (let [add-power (get-in game-state
                          [:cards (:card-id play) :add-power])
        target (:target play)]
    (if (nil? add-power)
        game-state
        (alter-card/add-power game-state target add-power))))

(defn apply-play
  "Applies an entire play"
  [game-state play]
  (-> game-state
      (apply-add-power play)
      (move-card (:card-id play) (:row-id play))))

(defn apply-all-plays
  "Plays all cards waiting to be played"
  [game-state]
    (-> game-state
        (apply-play (first (vals (:next-play game-state))))
        (apply-play (second (vals (:next-play game-state))))
        (assoc :next-play {})))

(defn  crowded-row?
  "True if a row has :limit cards for player-id"
  [game-state row-id player-id]
  (<= (get-in game-state [:rows row-id :limit] 9000)
      (count-cards/count-cards game-state {:location [:row row-id]
                                           :owner player-id})))

(defn ^:private requires-target?
  [game-state card-id]
  (some? (get-in game-state [:cards card-id :add-power])))

(defn play-card
  "Takes a playing of a card from hand onto a game row and makes it wait until both players had played"
  [game-state player-id card-id row-id & target]
  ; Uses stored :next-play to know who is supposed to play
  (cond 
    (some? (get-in game-state [:next-play (keyword player-id)]))
    {:error messages/out-of-turn}

    (not= (get-in game-state [:cards card-id :owner]) player-id)
    {:error messages/not-owned-card}

    (>= row-id (count (:rows game-state)))
    {:error messages/no-row}

    (crowded-row? game-state row-id player-id)
    {:error messages/row-limit}

    (and (requires-target? game-state card-id)
         (nil? target))
    {:error messages/need-target}

    (and (requires-target? game-state card-id)
         (not (identical? (get-in game-state [:cards (first target) :location 0]) :row)))
    {:error messages/invalid-target}

    :else
    (let [game-state (assoc-in game-state [:next-play (keyword player-id)] {:card-id card-id :row-id row-id :target (first target)})]
      (if (= 2 (count (:next-play game-state)))
        (apply-all-plays game-state)
        game-state))))
