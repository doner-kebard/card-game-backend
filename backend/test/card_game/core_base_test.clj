(ns card-game.core-base-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [card-game.test-helper :as helper]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (core/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (core/new-game))

  ; Hand's not empty
  (expect
    false
    (-> (core/new-game)
        (get-in [:players 0 :hand])
        (empty?))))

(defexpect points-on-rows
  ; We can get the current amount of points on different rows
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (victory-conditions/get-points)))

  (expect
    [[(helper/ini-hand-power 0) 0]
     [0 (helper/ini-hand-power 1)]
     [0 0] [0 0] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 1 1)
        (victory-conditions/get-points)))

  (expect
    [[(+ (helper/ini-hand-power 0) (helper/ini-hand-power 1))
      (helper/ini-hand-power 0)]
     [0 0] [0 0] 
     [0 (helper/ini-hand-power 1)] [0 0]]
    (-> (core/new-game)
        (core/play-card 0 0 0)
        (core/play-card 1 0 0)
        (core/play-card 0 0 0)
        (core/play-card 1 0 3)
        (victory-conditions/get-points))))
