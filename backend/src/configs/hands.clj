(ns configs.hands)

(def simple-hand
  "Simple hand for autoplay"
  (vec (repeat 42 {:power 10})))

(def default-hand
  "Default hand (can't be empty)"
  [{:power 10}
   {:power 9}
   {:power 8}
   {:power 7}
   {:power 6 :add-power 1}
   {:power 6 :add-power -1}
   {:power 4 :add-power 2}
   {:power 4 :add-power -2}])
