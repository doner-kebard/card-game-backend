(ns card-game.api-test
  (:use expectations
        card-game.api))

(expect
  #(contains? % :game-id)
  (create-game))

(expect
  #(contains? % :player-id)
  (create-game))

(expect
  #(= 12 (count (:hand %)))
  (create-game))

(expect
  #(= 5 (count (:rows %)))
  (create-game))

; Both stack all cards on one row -> tie
(expect
  #(= :tie (:winner %))
  (loop [game (create-game)
         opponent (add-player (:game-id game))
         iteration 12]
    (if (= 0 iteration)
      (get-game (:game-id game) (:player-id game))
      (recur
        (play-card-as-player (:game-id game) (:player-id game) 0 0)
        (play-card-as-player (:game-id game) (:player-id opponent) 0 0)
        (dec iteration)))))

; Opponent stacks all cards on one row and I spread -> I win
(expect
  #(= :me (:winner %))
  (loop [game (create-game)
         opponent (add-player (:game-id game))
         iteration 12]
    (if (= 0 iteration)
      (get-game (:game-id game) (:player-id game))
      (recur
        (play-card-as-player (:game-id game) (:player-id game) 0 (mod iteration 4))
        (play-card-as-player (:game-id game) (:player-id opponent) 0 0)
        (dec iteration)))))

; I stack all cards on one row and opponent spreads -> Opponent wins
(expect
  #(= :opponent (:winner %))
  (loop [game (create-game)
         opponent (add-player (:game-id game))
         iteration 12]
    (if (= 0 iteration)
      (get-game (:game-id game) (:player-id game))
      (recur
        (play-card-as-player (:game-id game) (:player-id game) 0 0)
        (play-card-as-player (:game-id game) (:player-id opponent) 0 (mod iteration 4))
        (dec iteration)))))

(expect
  #(empty? (filter (fn [e] (nil? (:power e))) %))
  (-> (create-game)
      :hand))

(expect
  #(= 11 (count (:hand %)))
  (let [game (create-game)]
    (play-card-as-player (:game-id game) (:player-id game) 0 0)))

(expect
  #(= 12 (count (:hand %)))
  (let [game (create-game)
        other (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id game) 0 0)
      (get-game (:game-id game) (:player-id other)))))

(expect
  #(= 11 (count (:hand %)))
  (let [game (create-game)
        other (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id game) 0 0)
      (get-game (:game-id game) (:player-id game)))))

(expect
  #(= :me (get-in % [:rows 0 0 :owner]))
  (let [game (create-game)]
    (play-card-as-player (:game-id game) (:player-id game) 0 0)))

(expect
  #(= :opponent (get-in % [:rows 0 0 :owner]))
  (let [game (create-game)
        other (add-player (:game-id game))]
    (do
      (play-card-as-player (:game-id game) (:player-id game) 0 0)
      (get-game (:game-id game) (:player-id other)))))

(expect
  #(contains? % :player-id)
  (-> (create-game)
      :game-id
      (add-player)))

(expect
  #(not (contains? % :error))
  (-> (create-game)
      :game-id
      (add-player)))

(expect
  #(= % (:game-id (add-player %)))
  (-> (create-game)
      :game-id))

; A third player causes an error
(expect
  {:error "Too many players"}
  (-> (create-game)
      :game-id
      (add-player)
      :game-id
      (add-player)))

(expect
  #(not (= (:player-id %) (:player-id (add-player (:game-id %)))))
  (create-game))
