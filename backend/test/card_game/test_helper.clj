(ns card-game.test-helper
  (:require [configs :as configs]))

(defn ini-hand-power
  [x]
  (get-in (configs/ini-hand) [x :power]))
