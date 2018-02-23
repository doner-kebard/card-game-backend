(ns card-game.effects-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]))

(defexpect card-altering
  ; Altering a card in hand works
  (expect
    [15 10 10 10 10 10 10 10 10 10 10 15]
    (map :power
         (-> (core/new-game)
             (core/alter-card [:players 0 :hand 0] {:power 15})
             (core/alter-card [:players 0 :hand 11] {:power 15})
             :players
             (nth 0)
             :hand)))

  ; Playing altered card goes as expected
  (expect
    {:power 15}
    (in
      (-> (core/new-game)
          (core/alter-card [:players 0 :hand 0] {:power 15})
          (core/play-card 0 0 0)
          (core/play-card 1 1 1)
          :rows (get 0) (get 0))))
  (expect
    [10 10 10 10 10 10 10 10 10 10 10]
    (map :power
         (-> (core/new-game)
             (core/alter-card [:players 0 :hand 0] {:power 15})
             (core/play-card 0 0 0)
             (core/play-card 1 1 1)
             :players
             (nth 0)
             :hand)))

  ; Altering a card in a row works
  (expect
    {:power 25}
    (in
      (-> (core/new-game)
          (core/play-card 0 0 0)
          (core/play-card 1 1 1)
          (core/alter-card [:rows 0 0] {:power 25})
          :rows (get 0) (get 0)))))
