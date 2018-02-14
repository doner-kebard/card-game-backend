(ns card-game.hand-test
  (:use expectations
        card-game.core))

; Players start with 12 cards in hand
(expect
  12
  (-> (new-game)
      :players
      first
      :hand
      count))
(expect
  12
  (-> (new-game)
      :players
      second
      :hand
      count))

; Players can be made to start with a different number of cards
(expect
  3
  (-> (new-game 3)
      :players
      first
      :hand
      count))

; Players are generated with default cards of power 10
(expect
  (repeat 12 10)
  (map :power
       (-> (new-game)
           :players
           first
           :hand)))


; Playing a card makes the card dissappear from the hand
(expect
  11
  (-> (new-game)
      (play-card 1 1 1)
      :players
      (nth 1)
      :hand
      count))

; Playing a card does not affect other players' hand
(expect
  12
  (-> (new-game)
      (play-card 1 1 1)
      :players
      (nth 0)
      :hand
      count))
