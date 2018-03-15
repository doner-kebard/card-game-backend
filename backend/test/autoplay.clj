(ns autoplay
  (:require [configs.hands :as hands]
            [api.base :as api]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]))

(defn as-api
  [strategy1 strategy2]
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)
        opponent-id (:player-id (api/add-player game-id))]
    (loop [game-state game
           iteration (count hands/default-hand)]
        (if (= 0 iteration)
          (api/get-game game-id player-id)
          (recur
            (do
              (api/play-card-as-player game-id player-id 0 (strategy1 iteration))
              (api/play-card-as-player game-id opponent-id 0 (strategy2 iteration)))
            (dec iteration))))))

(defn as-rules
  [strategy1 strategy2]
  (loop [game-state (create-game/new-game)
         iteration (count hands/default-hand)]
      (if (= 0 iteration)
        game-state
        (recur
          (-> game-state
              (play-card/play-card 0 0 (strategy1 iteration))
              (play-card/play-card 1 0 (strategy2 iteration)))
          (dec iteration)))))
