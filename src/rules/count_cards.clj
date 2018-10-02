(ns rules.count-cards)

(defn count-cards
  "Count (or sums the summands of) all cards that have keys as in condition "
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
