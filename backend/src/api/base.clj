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
  [game-id player index row-id & target]
  (let [game-state (persistence/fetch-game game-id)]
      (if (= (:status (get-game game-id player)) messages/play)
          (do
            (persistence/save-game (play-card/play-card game-state (conversions/player-num game-state player) index row-id (first target)))
            (get-game game-id player))
          {:error messages/out-of-turn})))

(defn ^:private create-empty-game
  "Creates a new instance of a game"
  [ini-config]
  (let [game-id (persistence/next-id)]
    (persistence/save-game
      (assoc (create-game/new-game ini-config)
             :game-id game-id))
    game-id))

(defn add-player
  "Adds a player to a game"
  [game-id]
  (let [saved-game (persistence/fetch-game game-id)
        players-connected (count (:player-ids saved-game))]
    (if (> players-connected 1)
      {:error messages/too-many-players}
      (let [game-state (persistence/save-game
                         (generators/player-uuid
                           saved-game))]
        (get-game game-id (last (:player-ids game-state)))))))

(defn create-game
  "Creates a new instance of a game with a player"
  ([] (create-game {}))
  ([ini-config]
   (-> (create-empty-game ini-config)
       (add-player))))
