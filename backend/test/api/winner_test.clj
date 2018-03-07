(ns api.winner-test
  (:require [expectations.clojure.test :refer :all]
            [api.base :as api]
            [configs.hand :as hand]))

(defn ^:private play-a-game-helper
  [strategy1 strategy2]
  (let [game (api/create-game)
        game-id (:game-id game)
        player-id (:player-id game)
        opponent-id (:player-id (api/add-player game-id))]
    (loop [game-state game
           iteration (count (hand/ini-hand))]
        (if (= 0 iteration)
          (api/get-game game-id player-id)
          (recur
            (do
              (api/play-card-as-player game-id player-id 0 (strategy1 iteration))
              (api/play-card-as-player game-id opponent-id 0 (strategy2 iteration)))
            (dec iteration))))))
    

(defexpect win-conditions
  ; Both stack all cards on one row -> tie
  (expect
    #(= :tie (:winner %))
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] 0)))

  ; Opponent stacks all cards on one row and I spread -> I win
  (expect
    #(= :me (:winner %))
    (play-a-game-helper
      (fn [i] (mod i 4))
      (fn [i] 0)))

  ; I stack all cards on one row and opponent spreads -> Opponent wins
  (expect
    #(= :opponent (:winner %))
    (play-a-game-helper
      (fn [i] 0)
      (fn [i] (mod i 4)))))
