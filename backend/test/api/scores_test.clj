(ns api.scores-test
  (:require [expectations.clojure.test :refer :all]
            [api.base :as api]
            [test-hand :as hand]
            [autoplay :as autoplay]
            [configs.hands :as hands]))

(defexpect rows-won
  (expect
    #(= [3 1] 
        (:scores %))
    (autoplay/as-api
      (fn [i] (mod i 4))
      (fn [i] 0)))
  (expect
    #(= [2 2]
        (:scores %))
    (autoplay/as-api
      (fn [i] (mod i 2))
      (fn [i] (+ (mod i 2) 2)))))

(defexpect power-in-rows
  ; Game begins with scores at 0
  (expect
    [[0 0] [0 0] [0 0] [0 0] [0 0]]
    (let [game-state (api/create-game)
          game-id (:game-id game-state)
          player-id (:player-id game-state)
          opponent-id (:player-id (api/add-player game-id))]
      (:rows-power (api/get-game game-id player-id))))
  (expect
    [[(hand/power-of-nth 0) (hand/power-of-nth 1)]
     [0 0] [0 0] [0 0] [0 0]]
    (let [game-state (api/create-game)
          game-id (:game-id game-state)
          player-id (:player-id game-state)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id player-id 0 0)
        (api/play-card-as-player game-id opponent-id 1 0))
      (:rows-power (api/get-game game-id player-id))))
  (expect
    [[0 (+ (hand/power-of-nth 0) (hand/power-of-nth 1))]
     [(hand/power-of-nth 0) 0]
     [(hand/power-of-nth 1) 0]
     [0 0] [0 0]]
    (let [game-state (api/create-game)
          game-id (:game-id game-state)
          player-id (:player-id game-state)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id player-id 0 0)
        (api/play-card-as-player game-id opponent-id 0 1)
        (api/play-card-as-player game-id player-id 0 0)
        (api/play-card-as-player game-id opponent-id 0 2)
      (:rows-power (api/get-game game-id opponent-id))))))
