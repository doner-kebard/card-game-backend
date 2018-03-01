(ns card-game.api-messages-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.api.base :as api]
            [configs :as configs]))

(defexpect status-check
  ; Game tracks status correctly
  (expect
    configs/no-opp
    (:status (api/create-game)))
  (expect
    configs/play
    (-> (api/create-game)
        :game-id
        (api/add-player)
        :status))
  (expect
    configs/no-opp
    (let [game (api/create-game)]
      (:status (api/get-game (:game-id game) (:player-id game)))))
  (expect
    configs/wait
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)]
      (do (api/add-player game-id)
          (:status
            (api/play-card-as-player game-id player-id 0 0)))))
 
  (expect
    configs/play
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (do (api/play-card-as-player game-id player-id 0 0)
          (:status
            (api/play-card-as-player game-id opponent-id 0 0)))))
 
  (expect
    configs/play
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (do (api/play-card-as-player game-id opponent-id 0 0)
          (:status
            (api/get-game game-id player-id)))))) 

(defexpect out-of-turn
  ; Game gives error when playing and shouldn't
  (expect
    {:error configs/out-of-turn}
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)]
      (do (api/add-player game-id)
          (api/play-card-as-player game-id player-id 0 0)
          (api/play-card-as-player game-id player-id 0 0))))
  
  (expect
    {:error configs/out-of-turn}
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)]
      (api/play-card-as-player game-id player-id 0 0))))      
