(ns card-game.build-card
  (:gen-class))

(defn build-card
  "Creates a new Card object"
  ([] (build-card {}))
  ([attributes] (merge {:power 10} attributes)))

