(ns card-game.api.base
  (:require [card-game.core :as core]
            [card-game.victory-conditions :as victory-conditions]
            [card-game.persistence :as persistence]
            [card-game.api.helper :as helper]
            [configs :as configs]))

(defn get-game
  "Fetches a game from an ID and returns the visible part as a player"
  [game-id player-id]
  (let [game-state (persistence/fetch-game game-id)]
  (assoc (helper/get-game-as-player game-state player-id)
         :status (helper/define-status game-state player-id))))

(defn play-card-as-player
  [game-id player index row]
  (let [game-state (persistence/fetch-game game-id)]
      (if (= (:status (get-game game-id player)) configs/play)
          (do
            (persistence/save-game (core/play-card game-state (helper/player-num game-state player) index row))
            (get-game game-id player))
          {:error configs/out-of-turn})))

(defn add-player
  "Adds a player to a game"
  [game-id]
  (let [saved-game (persistence/fetch-game game-id)
        players-connected (count (:player-ids saved-game))]
    (if (> players-connected 1)
      {:error configs/too-many-players}
      (let [game-state (persistence/save-game
                   (helper/create-player
                     (or
                       saved-game
                       (assoc (core/new-game) :game-id game-id))))]
        (get-game game-id (last (:player-ids game-state)))))))

(defn create-game
  "Creates a new instance of a game"
  [] (add-player (persistence/next-id)))
