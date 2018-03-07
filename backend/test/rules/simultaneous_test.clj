(ns rules.simultaneous-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory-conditions]
            [configs.messages :as messages]))

(defexpect out-of-turn
  ; Can't play a card when you were not supposed to
  (expect
    {:error messages/out-of-turn}
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 0 0 0))))

(defexpect next-play
  ; Stores next-play
  (expect
    {:player 0 :index 0 :row 0}
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (get-in [:next-play 0])))
  (expect
    {:player 1 :index 2 :row 3}
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        (play-card/play-card 1 2 3)
        (get-in [:next-play 1])))
  ; Stores nil when a play is expected
  (expect
    [nil nil]
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        :next-play))
  (expect
    [nil nil]
    (-> (create-game/new-game)
        :next-play))
  (expect
    nil
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (get-in [:next-play 1])))
  (expect
    nil
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        (play-card/play-card 1 2 3)
        (get-in [:next-play 0]))))


(defexpect update-only-when-both-play
  ; Doesn't updates game-state until both players played a card
  (expect
    [[] [] [] [] []]
    (-> (create-game/new-game)
        (play-card/play-card 0 0 0)
        :rows)))
