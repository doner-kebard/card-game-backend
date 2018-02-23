(ns card-game.build-card-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.build-card :refer :all]))

(defexpect card-builder
  (expect
    {:power 1}
    (in (build-card {:power 1})))

  ; default power
  (expect
    {:power 10}
    (in (build-card))))
