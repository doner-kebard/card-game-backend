(ns rules.alter-card)

(defn alter-card
  "Alters a cards' values, merging the new values with existing ones"
  [game-state card-id new-values]
  (update-in game-state [:cards card-id] #(merge % new-values)))

(defn add-power
  "Alters a cards' power, by adding the passed value to it.
  This power may be negative, resulting in a decrease"
  [game-state card-id increase]
  (let [power (get-in game-state [:cards card-id :power])]
    (alter-card game-state card-id {:power (+ power increase)})))
