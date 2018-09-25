(ns rules.abilities)

(defn add-power
  "Creates the ability function"
  [increase]
  (fn    
;    "Alters a cards' power, by adding the a defined value to it.
;    This power may be negative, resulting in a decrease"
    [game-state play]
    (update-in
      game-state
      [:cards (:target play) :power]
      #(+ % increase))))
