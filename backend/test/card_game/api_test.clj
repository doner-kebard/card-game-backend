(ns card-game.api-test
  (:require [expectations.clojure.test :refer :all]
            [card-game.api :as api]))

(defexpect sanity-check
  (expect
    #(contains? % :game-id)
    (api/create-game))
  (expect
    #(contains? % :player-id)
    (api/create-game))
  (expect
    #(= 12 (count (:hand %)))
    (api/create-game))
  (expect
    #(= 5 (count (:rows %)))
    (api/create-game)))

(defexpect win-conditions
  ; Both stack all cards on one row -> tie
  (expect
    #(= :tie (:winner %))
    (loop [game (api/create-game)
           opponent (api/add-player (:game-id game))
           iteration 12]
      (if (= 0 iteration)
        (api/get-game (:game-id game) (:player-id game))
        (recur
          (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
          (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
          (dec iteration)))))

  ; Opponent stacks all cards on one row and I spread -> I win
  (expect
    #(= :me (:winner %))
    (loop [game (api/create-game)
           opponent (api/add-player (:game-id game))
           iteration 12]
      (if (= 0 iteration)
        (api/get-game (:game-id game) (:player-id game))
        (recur
          (api/play-card-as-player (:game-id game) (:player-id game) 0 (mod iteration 4))
          (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
          (dec iteration)))))

  ; I stack all cards on one row and opponent spreads -> Opponent wins
  (expect
    #(= :opponent (:winner %))
    (loop [game (api/create-game)
           opponent (api/add-player (:game-id game))
           iteration 12]
      (if (= 0 iteration)
        (api/get-game (:game-id game) (:player-id game))
        (recur
          (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
          (api/play-card-as-player (:game-id game) (:player-id opponent) 0 (mod iteration 4))
          (dec iteration))))))

(defexpect playing-cards
  (expect
    #(empty? (filter (fn [e] (nil? (:power e))) %))
    (-> (api/create-game)
        :hand))

  ; Cards are removed from hands upon being played
  (expect
    #(= 11 (count (:hand %)))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
      (api/play-card-as-player (:game-id game) (:player-id game) 0 0)))

  ; Card is not removed if opponent has not yet played
  (expect
    #(= 12 (count (:hand %)))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
        (api/get-game (:game-id game) (:player-id game)))))

  ; Fetching the game as a player returns one less card after a play
  (expect
    #(= 11 (count (:hand %)))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id opponent) 0 0)
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0)
        (api/get-game (:game-id game) (:player-id game)))))

  ; card played is owned by self
  (expect
    #(= :me (get-in % [:rows 0 0 :owner]))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id opponent) 1 1)
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0))))

  ; opponent's card is owned by him
  (expect
    #(= :opponent (get-in % [:rows 1 0 :owner]))
    (let [game (api/create-game)
          opponent (api/add-player (:game-id game))]
      (do
        (api/play-card-as-player (:game-id game) (:player-id opponent) 1 1)
        (api/play-card-as-player (:game-id game) (:player-id game) 0 0)))))

(defexpect joining-a-game
  (expect
    #(contains? % :player-id)
    (-> (api/create-game)
        :game-id
        (api/add-player)))

  (expect
    #(not (contains? % :error))
    (-> (api/create-game)
        :game-id
        (api/add-player)))

  (expect
    #(= % (:game-id (api/add-player %)))
    (-> (api/create-game)
        :game-id))

  ; A third player causes an error
  (expect
    {:error "Too many players"}
    (-> (api/create-game)
        :game-id
        (api/add-player)
        :game-id
        (api/add-player)))

  (expect
    #(not (= (:player-id %) (:player-id (api/add-player (:game-id %)))))
    (api/create-game)))

(defexpect status-check
  ; Game tracks status correctly
  (expect
    "Waiting for an opponent"
    (:status (api/create-game)))
  (expect
    "Playing"
    (-> (api/create-game)
        :game-id
        (api/add-player)
        :status))
  (expect
    "Waiting for an opponent"
    (let [game (api/create-game)]
      (:status (api/get-game (:game-id game) (:player-id game)))))
  (expect
    "Waiting for opponent's play"
    (let [game (api/create-game)]
      (-> (:game-id game)
          (api/add-player)
          :game-id
          (api/play-card-as-player (:player-id game) 0 0)
          :status)))
  (expect
    "Playing"
    (let [game (api/create-game)
          opponent-id (:player-id (api/add-player (:game-id game)))]
      (-> (:game-id game)
          (api/play-card-as-player (:player-id game) 0 0)
          :game-id
          (api/play-card-as-player opponent-id 0 0)
          :status)))
  (expect
    "Playing"
    (let [game (api/create-game)
          opponent-id (:player-id (api/add-player (:game-id game)))]
      (-> (:game-id game)
          (api/play-card-as-player opponent-id 0 0)
          :game-id
          (api/get-game (:player-id game))
          :status))))

(defexpect out-of-turn
  ; Game gives error when playing and shouldn't
  (expect
    {:error "Out of turn play"}
    (let [game (api/create-game)]
      (-> (:game-id game)
          (api/add-player)
          :game-id
          (api/play-card-as-player (:player-id game) 0 0)
          :game-id
          (api/play-card-as-player (:player-id game) 0 0))))
  (expect
    {:error "Out of turn play"}
    (let [game (api/create-game)]
      (-> (:game-id game)
          (api/play-card-as-player (:player-id game) 0 0)))))
