(ns rules.abilities)

(defn ^:private row-affinity
  [row-type increase]
  (fn
;   "When played in a row-type row,
;   alters a its power, by adding the a defined value to it.
;   This power may be negative, resulting in a decrease"
    [gsp]
    (if (= (get-in gsp [:game-state :rows (:row-id (:play gsp)) :type])
           row-type)
      (update-in
        gsp
        [:game-state :cards (:card-id (:play gsp)) :power]
        #(+ % increase))
      gsp)))

(defn ^:private strengthen
  [increase]
  (fn    
;   "Alters a cards' power, by adding the a defined value to it.
;   This power may be negative, resulting in a decrease"
    [gsp]
    (update-in
      gsp
      [:game-state :cards (:target (:play gsp)) :power]
      #(+ % increase))))

(defn ^:private weaken
  "Same as strengthen but negative"
  [decrease]
  (strengthen (- decrease)))

(def ^:private ability-list
  {:row-affinity row-affinity
   :strengthen strengthen
   :weaken weaken})

(def ^:private targeted-abilities
  '(:strengthen :weaken))

(defn ^:private generate-ability-fn
  "Creates an ability function from its description"
  [ability-description]
  (let [ability-generator ((first ability-description) ability-list)
        args (vec (rest ability-description))]
    (apply ability-generator args)))

(defn ^:private generate-gsp
  "Merges game-state and play into gsp (game-state-play)"
  [game-state play]
  {:game-state game-state :play play})

(defn generate-abilities-fn
  "Creates a function that collapses all ability functions"
  [abilities-vector]
  (comp :game-state (apply comp (map generate-ability-fn abilities-vector)) generate-gsp))

(defn required-targets
  "Counts the required targets"
  [abilities-vector]
  (loop [abilities abilities-vector
         needed-targets 0]
    (if (empty? abilities)
      needed-targets
      (recur
        (rest abilities)
        (if (contains? (set targeted-abilities) (first (first abilities)))
          (inc needed-targets)
          needed-targets)))))
