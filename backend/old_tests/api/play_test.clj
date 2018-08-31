(ns api.play-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as api]
            [configs.hands :as hands]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect playing-cards
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)
        opponent (api/add-player game-id)
        opponent-id (:player-id opponent)]
    ; Cards in hand have power
    (expect
      #(empty? (filter (fn [e] (nil? (:power e))) %))
      (:hand game))

    ; Card is not removed if opponent has not yet played
    (expect
      #(= (count (:hand %)) (count hands/default-hand))
      (do
        (api/play-card-as-player game-id player-id 0 0)
        (api/get-game game-id player-id)))

    ; Cards are removed from hands upon both having played
    (expect
      #(= (count (:hand %)) (dec (count hands/default-hand)))
      (api/play-card-as-player game-id opponent-id 0 1))

    ; card played is owned by self
    (expect
      #(= :me (get-in % [:rows 0 :cards 0 :owner]))
      (api/get-game game-id player-id))

    ; opponent's card is owned by him
    (expect
      #(= :opponent (get-in % [:rows 1 :cards 0 :owner]))
      (api/get-game game-id player-id))))
