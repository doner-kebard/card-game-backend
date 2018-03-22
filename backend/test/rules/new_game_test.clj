(ns rules.new-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory]
            [configs.hands :as hands]
            [configs.rows :as rows]
            [autoplay :as autoplay]))

(defexpect basic.game
  ; Game can be created
  (expect
    #(not (nil? %))
    (create-game/new-game))

  ; Game contains two players
  (expect
    #(count (:players %))
    (create-game/new-game))

  ; Hand's not empty
  (expect
    false
    (-> (create-game/new-game)
        (get-in [:players 0 :hand])
        (empty?)))

  ; Rows begin empty and with limit
  (expect
    (repeat 5 {:limit rows/default-limit :cards []})
    (-> (create-game/new-game)
        :rows)))

(defexpect starting-hand
  ; Players start with expected hand
  (expect
    hands/default-hand
    (-> (create-game/new-game)
        :players
        first
        :hand))
  (expect
    hands/default-hand
    (-> (create-game/new-game)
        :players
        second
        :hand)))

(defexpect configs.game
  ; Game can start with different configs
  (expect
    [{:power 0 :attribute 9001}{:power 1}]
    (-> (create-game/new-game {:hand [{:power 0 :attribute 9001}
                                      {:power 1}]})
        (get-in [:players 0 :hand]))))

(defexpect next-play
  ; Game starts with a nil next-play
  (expect
    [nil nil]
    (-> (create-game/new-game)
        :next-play)))
