(ns api.player-view
  (:require [api.player-view-functions :as functions]))

(defn get-game-as-player
  "Returns the part of the game-state that ought to be visible to the player"
  [game-state player-id]
  {:game-id (:game-id game-state)
   :player-id player-id
   :cards (functions/get-cards game-state player-id)
   :rows (functions/get-rows game-state player-id)
   :scores (functions/get-scores game-state player-id)
   :game-status (functions/get-game-status game-state player-id)
   :winner (functions/get-winner game-state player-id)})
