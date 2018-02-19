(ns card-game.api
  (:require [taoensso.carmine :as car :refer (wcar)])
  (:use card-game.core))

; See `wcar` docstring for opts
(def server1-conn
  {:pool {} :spec {:uri "redis://redis"}})

(defmacro wcar*
  [& body] 
  `(car/wcar server1-conn ~@body))

(defn set-game-id
  "Saves an ID and returns it for convenience"
  [id] (do
         (wcar* (car/set "next-game-id" id))
         id))

(defn next-id
  "Returns the next available game id"
  []
  (let [id (wcar* (car/parse-int (car/get "next-game-id")))]
    (if (nil? id)
      (set-game-id 0)
      (set-game-id (inc id)))))

(defn save-game
  "Saves a game"
  [game]
  (wcar* (car/set {:game (:game-id game)} game))
  game)

(defn fetch-game
  [id] (wcar* (car/get {:game id})))

(defn player-num
  [game id] (.indexOf (:player-ids game) id))

(defn get-game-as-player
  "Returns the part that ought to be visible to the player"
  [game player]
  {:game-id (:game-id game)
   :player-id player
   :hand (get-in game [:players (player-num game player) :hand])
   :rows (get-in game [:rows])})

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
