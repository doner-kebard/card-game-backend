(ns card-game.hand-test
  (:use expectations
        card-game.core))

; Players start with 12 cards in hand
(expect
  #(= (count (:hand %)) 12)
  (-> (new-game)
      :players
      first))

(expect
  #(= (count (:hand %)) 12)
  (-> (new-game)
      :players
      second))

; Players can be made to start with a different number of cards
(expect
  #(= (count (:hand %)) 3)
  (-> (new-game 3)
      :players
      second))

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
  #(= (count (:hand %)) 11)
  (-> (new-game)
      (play-card 1 1 1)
      :players
      second))

; Playing a card does not affect other players' hand
(expect
  #(= (count (:hand %)) 12)
  (-> (new-game)
      (play-card 1 1 1)
      :players
      first))
