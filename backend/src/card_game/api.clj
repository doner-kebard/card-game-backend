(ns card-game.api
  (:use card-game.core
        card-game.victory-conditions
        card-game.persistence))

(defn player-num
  "Translates a Player id into an internal player representation"
  [game-state id] (.indexOf (:player-ids game-state) id))

(defn translate-player
  "Translates an internal player to a useful representation"
  [game-state internal me]
  (cond
    (nil? internal) nil
    (>= internal 2) :tie
    (= me (get-in game-state [:player-ids internal])) :me
    :else :opponent))

(defn get-game-as-player
  "Returns the part that ought to be visible to the player"
  [game-state player-id]
  {:game-id (:game-id game-state)
   :player-id player-id
   :hand (get-in game-state [:players (player-num game-state player-id) :hand])
   :rows (mapv #(mapv (fn [card]
                        (assoc card :owner
                          (translate-player game-state (:owner card) player-id)))
                      %)
               (:rows game-state))
   :winner (translate-player game-state (winner game-state) player-id)})

(defn define-status
  "Returns the status of the game"
  [game-state player-id]
  (cond (= (count (:player-ids game-state)) 1) "Waiting for an opponent"
        (get-in game-state [:play-wanted (player-num game-state player-id)]) "Playing"
        :else "Waiting for opponent's play"))

(defn get-game
  "Fetches a game from an ID and returns the visible part as a player"
  [game-id player-id]
  (let [game-state (fetch-game game-id)]
  (assoc (get-game-as-player game-state player-id)
         :status (define-status game-state player-id))))

(defn play-card-as-player
  [game-id player index row]
  (let [game-state (fetch-game game-id)]
      (if (= (:status (get-game game-id player)) "Playing")
          (do
            (save-game (play-card game-state (player-num game-state player) index row))
            (get-game game-id player))
          {:error "Out of turn play"})))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn create-player
  [game]
  (loop [id (uuid)]
    (if (some #{id} (:player-ids game))
      (recur (uuid))
      (update game :player-ids #(vec (conj % id))))))

(defn add-player
  "Adds a player to a game"
  [game-id]
  (let [saved-game (fetch-game game-id)
        players-connected (count (:player-ids saved-game))]
    (if (> players-connected 1)
      {:error "Too many players"}
      (let [game-state (save-game
                   (create-player
                     (or
                       saved-game
                       (assoc (new-game) :game-id game-id))))]
        (get-game game-id (last (:player-ids game-state)))))))

(defn create-game
  "Creates a new instance of a game"
  [] (add-player (next-id)))
