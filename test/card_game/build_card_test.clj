(ns card-game.build-card-test
  (:use expectations
        card-game.build-card))

(expect
  1
  (-> (build-card {:power 1})
      :power))

; default power
(expect
  10
  (-> (build-card)
      :power))
