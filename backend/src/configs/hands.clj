(ns configs.hands
  (:require [rules.abilities :as ability]))

(def default-hand
  "Default hand (can't be empty)"
  [{:power 8}
   {:power 7}
   (merge {:power 6} (ability/type-add-power "overpower" 1))
   (merge {:power 5} (ability/type-add-power "overpower" 3))
   (merge {:power 6} (ability/add-power 1))
   (merge {:power 6} (ability/add-power -1))
   (merge {:power 4} (ability/add-power 2))
   (merge {:power 4} (ability/add-power -2))])

(def default-hands
  "Default hands"
  [default-hand default-hand])
