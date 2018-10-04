(ns rules.abilities)

(defn ^:private row-affinity
  [row-type increase]
  (fn
;   "When played in a row-type row,
;   alters a its power, by adding the a defined value to it.
;   This power may be negative, resulting in a decrease"
    [game-state play]
    (if (= (get-in game-state [:rows (:row-id play) :type])
           row-type)
      (update-in
        game-state
        [:cards (:card-id play) :power]
        #(+ % increase))
      game-state)))

(defn ^:private strengthen
  [increase]
  (fn    
;   "Alters a cards' power, by adding the a defined value to it.
;   This power may be negative, resulting in a decrease"
    [game-state play]
    (update-in
      game-state
      [:cards (:target play) :power]
      #(+ % increase))))

(defn ^:private weaken
  "Same as strengthen but negative"
  [decrease]
  (strengthen (- decrease)))

(defn generate-ability-fn
  "Creates an ability function from its description"
  [ability-description]
  (let [ability-generator (ns-resolve 'rules.abilities (symbol (name (first ability-description))))
        args (vec (rest ability-description))]
    (apply ability-generator args)))

(defn required-targets
  "Counts the required targets"
  [ability-description]
  (if (contains? (set '(:strengthen :weaken)) (first ability-description))
    1
    0))
