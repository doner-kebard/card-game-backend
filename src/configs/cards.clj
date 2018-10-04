(ns configs.cards
  (:require [rules.abilities :as ability]))

(def card-map
  "All possible cards"
  {:8 {:power 8}
   :7 {:power 7}
   :6-overpower {:power 6 :abilities [:row-affinity "overpower" 1]}
   :5-overpower {:power 5 :abilities [:row-affinity "overpower" 3]}
   :6-strengthen {:power 6 :abilities [:strengthen 1]}
   :6-weaken {:power 6 :abilities [:weaken 1]}
   :4-strengthen {:power 4 :abilities [:strengthen 2]}
   :4-weaken {:power 4 :abilities [:weaken 2]}})
