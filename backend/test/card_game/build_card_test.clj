(ns card-game.build-card-test
  (:use expectations
        card-game.build-card))

(expect
  {:power 1}
  (in (build-card {:power 1})))

; default power
(expect
  {:power 10}
  (in (build-card)))
