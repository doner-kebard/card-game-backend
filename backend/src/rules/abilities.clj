(ns rules.abilities)

(defn add-power
  "Creates the ability function"
  [increase]
  (fn    
;    "Alters a card's power, by adding the a defined value to it.
;    This power may be negative, resulting in a decrease"
    [game-state play]
    (update-in
      game-state
      [:cards (:target play) :power]
      #(+ % increase))))

(defn type-add-power
  "Creates the ability function"
  [row-type increase]
  (fn
;    "When played in a row-type row,
;    alters a its power, by adding the a defined value to it.
;    This power may be negative, resulting in a decrease"
    [game-state play]
    (if (= (get-in game-state [:rows (:row-id play) :type])
           row-type)
      (update-in
        game-state
        [:cards (:card-id play) :power]
        #(+ % increase))

      game-state)))
