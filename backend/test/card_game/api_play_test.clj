(ns card-game.api-play-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.api.base :as api]
            [configs :as configs]))

(defexpect playing-cards
  ; Cards in hand have power
  (expect
    #(empty? (filter (fn [e] (nil? (:power e))) %))
    (-> (api/create-game)
        :hand))

  ; Cards are removed from hands upon being played
  (expect
    #(= (count (:hand %)) (dec (count (configs/ini-hand))))
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id opponent-id 0 0)
        (api/play-card-as-player game-id player-id 0 0))))

  ; Card is not removed if opponent has not yet played
  (expect
    #(= (count (:hand %)) (count (configs/ini-hand)))
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)]
      (do
        (api/add-player game-id)
        (api/play-card-as-player game-id player-id 0 0)
        (api/get-game game-id player-id))))

  ; Fetching the game as a player returns one less card after a play
  (expect
    #(= (count (:hand %)) (dec (count (configs/ini-hand))))
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id opponent-id 0 0)
        (api/play-card-as-player game-id player-id 0 0)
        (api/get-game game-id player-id ))))

  ; card played is owned by self
  (expect
    #(= :me (get-in % [:rows 0 0 :owner]))
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id opponent-id 1 1)
        (api/play-card-as-player game-id player-id 0 0))))

  ; opponent's card is owned by him
  (expect
    #(= :opponent (get-in % [:rows 1 0 :owner]))
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)
          opponent-id (:player-id (api/add-player game-id))]
      (do
        (api/play-card-as-player game-id opponent-id 1 1)
        (api/play-card-as-player game-id player-id 0 0)))))
