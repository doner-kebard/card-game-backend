(ns test-hand
  (:require [configs.hands :as hands]))

(defn power-of-nth
  ([x]
    (power-of-nth x hands/default-hand))
  ([x hand]
    (get-in hand [x :power])))
