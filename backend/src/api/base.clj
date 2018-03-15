(ns api.base
  (:require [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [persistence.persistence :as persistence]
            [api.player-view :as player-view]
            [api.conversions :as conversions]
            [api.generators :as generators]
            [configs.messages :as messages]))

(defn get-game
  "Fetches a game from an ID and returns the visible part as a player"
  [game-id player-id]
  (player-view/get-game-as-player (persistence/fetch-game game-id) player-id))

(defn play-card-as-player
  [game-id player index row]
  (let [game-state (persistence/fetch-game game-id)]
      (if (= (:status (get-game game-id player)) messages/play)
          (do
            (persistence/save-game (play-card/play-card game-state (conversions/player-num game-state player) index row))
            (get-game game-id player))
          {:error messages/out-of-turn})))

(defn add-player
  "Adds a player to a game"
  [game-id]
  (let [saved-game (persistence/fetch-game game-id)
        players-connected (count (:player-ids saved-game))]
    (if (> players-connected 1)
      {:error messages/too-many-players}
      (let [game-state (persistence/save-game
                   (generators/player-uuid
                     (or
                       saved-game
                       (assoc (create-game/new-game) :game-id game-id))))]
        (get-game game-id (last (:player-ids game-state)))))))

(defn create-game
  "Creates a new instance of a game"
  [] (add-player (persistence/next-id)))
