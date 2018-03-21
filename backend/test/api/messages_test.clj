(ns api.messages-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as api]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect no-opp-on-creation
  (let [game (api/create-game)]
    (expect
      messages/no-opp
      (:status game))
    (expect
      messages/no-opp
      (:status
        (api/get-game (:game-id game) (:player-id game))))))

(defexpect play-on-both-players
  (expect
    messages/play
    (-> (api/create-game)
        :game-id
        (api/add-player)
        :status)))

(defexpect wait-on-card-played
  (expect
    messages/wait
    (let [game (api/create-game)
          game-id (:game-id game)
          player-id (:player-id game)]
      (do (api/add-player game-id)
          (:status
            (api/play-card-as-player game-id player-id 0 0))))))

(defexpect play-on-not-played
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)
        opponent-id (:player-id (api/add-player game-id))]
    (expect
      messages/play
      (do (api/play-card-as-player game-id player-id 0 0)
          (:status
            (api/play-card-as-player game-id opponent-id 0 0))))

    (expect
      messages/play
      (do (api/play-card-as-player game-id opponent-id 0 0)
          (:status
            (api/get-game game-id player-id))))))

(defexpect out-of-turn
  ; Game gives error when playing and shouldn't
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)]

    (expect
      {:error messages/out-of-turn}
      (api/play-card-as-player game-id player-id 0 0))

    (expect
      {:error messages/out-of-turn}
      (do (api/add-player game-id)
          (api/play-card-as-player game-id player-id 0 0)
          (api/play-card-as-player game-id player-id 0 0)))))


