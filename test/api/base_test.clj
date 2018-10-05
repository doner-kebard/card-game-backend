(ns api.base-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as base]
            [configs.hands :as hands]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect game-creation
  (expect number? (:game-id (base/create-game)))
  (expect false (=
                 (:game-id (base/create-game))
                 (:game-id (base/create-game))))
  (expect messages/no-opp (:lobby-status (base/create-game)))
  (expect nil (:player-ids (base/create-game)))
  (expect true (contains? (base/create-game) :player-id)))

(defexpect join-game

  (let [joined-game (-> (base/create-game) :game-id base/add-player)]
    (expect number? (:game-id joined-game))
    (expect (count hands/default-hand)
            (count (filter
                     #(= "me" (:owner %))
                     (:cards joined-game))))
    (expect (count hands/default-hand)
            (count (filter
                     #(= "opp" (:owner %))
                     (:cards joined-game))))))

(defexpect join-crowded
  (expect {:error messages/too-many-players}
          (-> (base/create-game)
              :game-id
              base/add-player
              :game-id
              base/add-player)))

(defexpect join-empty
  (expect {:error messages/lobby-not-created}
              (base/add-player 0)))

(defexpect play-card-messages

  (let [game (base/create-game)
        game-id (:game-id game)
        p1 (:player-id game)
        p2 (:player-id (base/add-player game-id))]
    
    (expect {:error messages/invalid-id}
            (base/play-card-as-player 9999999999 p1 0 0))
    
    (expect {:error messages/invalid-id}
            (base/play-card-as-player game-id "MrInvalid" 0 0))
    
    (expect {:error messages/no-row}
            (base/play-card-as-player game-id p1 1 99))
            
    (expect {:error messages/not-owned-card}
            (base/play-card-as-player game-id p1 15 0))
    
    (expect {:error messages/not-owned-card}
            (base/play-card-as-player game-id p2 0 0))

    (expect {:error messages/need-target}
            (base/play-card-as-player game-id p1 4 0))

    (expect {:error messages/invalid-target}
            (base/play-card-as-player game-id p1 4 0 4))
    
    (expect messages/wait
            (:game-status (base/play-card-as-player game-id p1 1 0)))

    (expect {:error messages/out-of-turn}
            (base/play-card-as-player game-id p1 1 0))

    (expect messages/play
            (:game-status (base/play-card-as-player game-id p2 11 2)))

    (expect messages/wait
            (:game-status (base/play-card-as-player game-id p2 11 1)))))
