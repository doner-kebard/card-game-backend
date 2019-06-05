(ns api.base
  (:require [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [persistence.persistence :as persistence]
            [api.player-view :as player-view]
            [api.generators :as generators]
            [configs.messages :as messages]))

(defn ^:private valid-game-state
  "Returns game-state if valid ids. Returns an error otherwise."
  [game-id player-id]
  (let [game-state (persistence/fetch-game game-id)]
    (cond (nil? game-state)
          {:error messages/invalid-id}
          (not (or (= player-id (first (:player-ids game-state)))
                   (= player-id (second (:player-ids game-state)))))
          {:error messages/invalid-id}
          :else
          game-state)))

(defn get-game
  "Fetches a game from an ID and returns the visible part as a player if appropiate"
  [game-id player-id]
  (let [game-state (valid-game-state game-id player-id)]
    (if (contains? game-state :error)
        game-state
        (player-view/get-game-as-player game-state player-id))))

(defn play-card-as-player
  "Plays a card give and returns the game sate as seen by the player"
  [game-id player-id card-id row-id & target]
  (let [game-state (valid-game-state game-id player-id)]
    (if (contains? game-state :error)
      game-state
      (if (= (:game-status (get-game game-id player-id)) messages/play)
          (let [new-game-state (play-card/play-card game-state player-id card-id row-id (first target))]
            (if (contains? new-game-state :error)
              new-game-state
              (do
                (persistence/save-game new-game-state)
                (get-game game-id player-id))))
          {:error messages/out-of-turn}))))

(defn ^:private create-empty-game
  "Creates a new instance of a game lobby"
  []
  (let [game-id (persistence/next-id)]
    (persistence/save-game
      {:lobby-status messages/no-opp
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
                  :game-id
                  (get-game second-uuid))))))

(defn create-game
  "Creates a new instance of a game with a player"
  []
  (let [lobby (create-empty-game)]
    (-> lobby
        (assoc :player-id (first (:player-ids lobby)))
        (dissoc :player-ids))))
