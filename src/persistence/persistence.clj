(ns persistence.persistence
  (:require [taoensso.nippy :as nippy]
            [clojure.java.io :as io]))

(def persistence-prefix "data/")

(defn ^:private get-persistent
  [file]
  (let [target (str persistence-prefix file)]
    (nippy/thaw (.getBytes (slurp target)))))

(defn ^:private set-persistent
  [file info]
  (let [target (str persistence-prefix file)]
    (io/make-parents target)
    (spit target (slurp (nippy/freeze info)))))

(defn ^:private get-id []
  (try (get-persistent "next-game-id") (catch Exception e 0)))

(defn ^:private set-game-id
  "Saves an ID and returns it for convenience"
  [id] (do
         (set-persistent "next-game-id" id)
         id))

(defn next-id
  "Returns the next available game id"
  []
  (let [id (get-id)]
    (set-game-id (inc id))
    id))

(defn save-game
  "Saves a game"
  [game]
  (set-persistent (str "games/" (:game-id game)) game)
  game)

(defn fetch-game
  [id] (get-persistent (str "games/" id)))
