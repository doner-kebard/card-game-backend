(ns rules.effects-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.alter-card :as alter-card]
            [configs.hand :as hand]))

(defexpect card-altering
  ; Altering a card in hand works
  (expect
    (concat [9001] (rest (vec (map :power (hand/ini-hand)))))
    (map :power
         (-> (create-game/new-game)
             (alter-card/alter-card [:players 1 :hand 0] {:power 9001})
             :players
             (nth 1)
             :hand)))

  (expect
    (concat [9001]
            (subvec (vec (map :power (hand/ini-hand)))
                    1 (dec (count (hand/ini-hand))))
            [9002])
    (map :power
         (-> (create-game/new-game)
             (alter-card/alter-card [:players 0 :hand 0] {:power 9001})
             (alter-card/alter-card [:players 0 :hand (dec (count (hand/ini-hand)))] {:power 9002})
             :players
             (nth 0)
             :hand)))

  ; Playing altered card goes as expected
  (expect
    {:power 9001}
    (in
      (-> (create-game/new-game)
          (alter-card/alter-card [:players 0 :hand 0] {:power 9001})
          (play-card/play-card 0 0 0)
          (play-card/play-card 1 1 1)
          :rows (get 0) (get 0))))
  (expect
    (rest (map :power (hand/ini-hand)))
    (map :power
         (-> (create-game/new-game)
             (alter-card/alter-card [:players 0 :hand 0] {:power 9001})
             (play-card/play-card 0 0 0)
             (play-card/play-card 1 1 1)
             :players
             (nth 0)
             :hand)))

  ; Altering a card in a row works
  (expect
    {:power 9001}
    (in
      (-> (create-game/new-game)
          (play-card/play-card 0 0 0)
          (play-card/play-card 1 1 1)
          (alter-card/alter-card [:rows 0 0] {:power 9001})
          :rows (get 0) (get 0)))))
