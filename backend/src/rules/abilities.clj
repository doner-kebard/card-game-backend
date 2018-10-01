(ns rules.abilities)

(defn add-power
  "Creates the ability function"
  [increase]
  (fn    
;   "Alters a cards' power, by adding the a defined value to it.
;   This power may be negative, resulting in a decrease"
    ([game-state play]
     (update-in
       game-state
       [:cards (:target play) :power]
       #(+ % increase)))
     ([]
      {:description (if (neg? increase)
                      (str "Weaken " (- increase))
                      (str "Enhance " increase))
       :target 1})))

(defn type-add-power
  "Creates the ability function"
  [row-type increase]
  (fn
;    "When played in a row-type row,
;    alters a its power, by adding the a defined value to it.
;    This power may be negative, resulting in a decrease"
    ([game-state play]
     (if (= (get-in game-state [:rows (:row-id play) :type])
            row-type)
       (update-in
         game-state
         [:cards (:card-id play) :power]
         #(+ % increase))

       game-state))
    ([]
     {:description (if (neg? increase)
                     (str "Weaken " (- increase) " on " type)
                     (str "Enhance " increase "on " type))})))
