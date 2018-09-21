(ns rules.abilities)

(defn add-power
  "Creates the ability function"
  [increase]
  (fn    
;    "Alters a cards' power, by adding the a defined value to it.
;    This power may be negative, resulting in a decrease"
    [game-state target]
    (update-in
      game-state
      [:cards target :power]
      #(+ % increase))))
