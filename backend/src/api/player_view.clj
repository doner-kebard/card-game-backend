(ns api.player-view
  (:require [rules.victory-conditions :as victory]
            [api.player-view-functions :as functions]
            [configs.messages :as messages]))

(defn get-game-as-player
  "Returns the part of the game-state that ought to be visible to the player"
  [game-state player-id]
  {:game-id (:game-id game-state)
   :player-id player-id
   :hand (functions/get-hand game-state player-id)
   :rows (functions/get-rows game-state player-id)
   :rows-power (functions/get-rows-power game-state player-id)
   :scores (functions/get-scores game-state player-id)
   :status (functions/get-status game-state player-id)
   :winner (functions/get-winner game-state player-id)})
