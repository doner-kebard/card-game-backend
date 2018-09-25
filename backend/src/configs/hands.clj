(ns configs.hands
  (:require [rules.abilities :as ability]))

(def default-hand
  "Default hand (can't be empty)"
  [{:power 8}
   {:power 7}
   {:power 6 :ability (ability/type-add-power "overpower" 1)}
   {:power 5 :ability (ability/type-add-power "overpower" 3)}
   {:power 6 :ability (ability/add-power 1)}
   {:power 6 :ability (ability/add-power -1)}
   {:power 4 :ability (ability/add-power 2)}
   {:power 4 :ability (ability/add-power -2)}])

(def default-hands
  "Default hands"
  [default-hand default-hand])
