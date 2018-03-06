(ns rules.base-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory-conditions]
            [test-helper :as helper]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (create-game/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (create-game/new-game))

  ; Hand's not empty
  (expect
    false
    (-> (create-game/new-game)
        (get-in [:players 0 :hand])
        (empty?))))

(defexpect points-on-rows
  ; We can get the current amount of points on different rows
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (-> (create-game/new-game)
        (victory-conditions/get-points)))

  (expect
    [[(helper/ini-hand-power 0) 0]
     [0 (helper/ini-hand-power 1)]
     [0 0] [0 0] [0 0]]
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        (victory-conditions/get-points)))

  (expect
    [[(+ (helper/ini-hand-power 0) (helper/ini-hand-power 1))
      (helper/ini-hand-power 0)]
     [0 0] [0 0] 
     [0 (helper/ini-hand-power 1)] [0 0]]
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 0 0)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 0 3)
        (victory-conditions/get-points))))
