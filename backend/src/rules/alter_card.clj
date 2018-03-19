(ns rules.alter-card)

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state path new-values]
  (update-in game-state path #(merge % new-values)))

(defn add-power
  "Alters a cards' power, by adding the passed value to it.
  This power may be negative, resulting in a decrease"
  [game-state path increase]
  (let [power (get-in game-state (conj path :power))]
    (alter-card game-state path {:power (+ power increase)})))
