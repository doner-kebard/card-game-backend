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
