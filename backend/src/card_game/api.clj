(ns card-game.api
  (:use card-game.core
        card-game.victory-conditions
        card-game.persistence))

(defn player-num
  "Translates a Player id into an internal player representation"
  [game id] (.indexOf (:player-ids game) id))

(defn translate-player
  "Translates an internal player to a useful representation"
  [game internal me]
  (cond
    (nil? internal) nil
    (>= internal 2) :tie
    (= me (get-in game [:player-ids internal])) :me
    :else :opponent))

(defn get-game-as-player
  "Returns the part that ought to be visible to the player"
  [game player]
  {:game-id (:game-id game)
   :player-id player
   :hand (get-in game [:players (player-num game player) :hand])
   :rows (mapv #(mapv (fn [card]
                        (assoc card :owner
                          (translate-player game (:owner card) player)))
                      %)
               (:rows game))
   :winner (translate-player game (winner game) player)})

(defn get-game
  "Fetches a game from an ID and returns the visible part as a player"
  [id player]
  (get-game-as-player (fetch-game id) player))

(defn play-card-as-player
  [game-id player index row]
  (let [game (fetch-game game-id)]
  (do
    (save-game (play-card game (player-num game player) index row))
    (get-game game-id player))))

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
  (let [saved-game (fetch-game game-id)]
    (if (> (count (:player-ids saved-game)) 1)
      {:error "Too many players"}
      (let [game (save-game 
                   (create-player
                     (or 
                       saved-game 
                       (assoc (new-game) :game-id game-id))))]
        (get-game-as-player game (last (:player-ids game)))))))

(defn create-game
  "Creates a new instance of a game"
  [] (add-player (next-id)))
