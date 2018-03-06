(ns test-helper
  (:require [configs.hand :as hand]))

(defn ini-hand-power
  [x]
  (get-in (hand/ini-hand) [x :power]))
