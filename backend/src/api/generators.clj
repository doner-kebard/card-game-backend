(ns api.generators)

(defn ^:private uuid [] (str (java.util.UUID/randomUUID)))

(defn player-uuid
  [game]
  (loop [id (uuid)]
    (if (some #{id} (:player-ids game))
      (recur (uuid))
      id)))
