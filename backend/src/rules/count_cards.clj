(ns rules.count-cards)

(defn count-cards
  "Count all cards that has keys as in condition an sums their summand"
  [game-state condition & summand]
  (let [k (vec (keys condition))
        summand (first summand)]
    (reduce
      #(if (= (select-keys %2 k) condition)
           (if (some? summand)
               (+ %1 (summand %2 0))
               (inc %1))
           %1)
      0
      (:cards game-state))))
