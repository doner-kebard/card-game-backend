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
  [game-id player-id card-id row-id & target]
  (let [game-state (persistence/fetch-game game-id)]
      (if (= (:status (get-game game-id player-id)) messages/play)
          (do
            (persistence/save-game (play-card/play-card game-state player-id card-id row-id (first target)))
            (get-game game-id player-id))
          {:error messages/out-of-turn})))

(defn ^:private create-empty-game
  "Creates a new instance of a game lobby"
  [ini-config]
  (let [game-id (persistence/next-id)]
    (persistence/save-game
      {:status messages/no-opp
       :game-id game-id
       :player-ids [(generators/player-uuid {})]})))

(defn add-player
  "Adds a player to a game"
  [game-id]
  (let [lobby (persistence/fetch-game game-id)
        players-connected (count (:player-ids lobby))]
    (cond
      (> players-connected 1) {:error messages/too-many-players}
      (< players-connected 1) {:error messages/lobby-not-created}
      :else (let [second-uuid (generators/player-uuid lobby)]
              (-> (create-game/new-game
                    {:player-ids [(first (:player-ids lobby))
                                  second-uuid]})
                  (assoc :game-id (:game-id lobby))
                  persistence/save-game
                  (get-game second-uuid))))))

(defn create-game
  "Creates a new instance of a game with a player"
  ([] (create-game {}))
  ([ini-config]
   (let [lobby (create-empty-game ini-config)]
     (-> lobby
         (assoc :player-id (first (:player-ids lobby)))
         (dissoc :player-ids)))))
