(ns persistence.persistence
  (:require [taoensso.carmine :as car :refer (wcar)]))

; See `wcar` docstring for opts
(def server1-conn
  {:pool {} :spec {:uri (or
                          (System/getenv "CARD_GAME_DB")
                          "redis://localhost")}})

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
