(ns rules.new-game-test
  (:require [expectations.clojure.test :refer :all]
            [rules.create-game :as create-game]
            [rules.play-card :as play-card]
            [rules.victory-conditions :as victory]
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

  ; Rows begin empty
  (expect
    [[] [] [] [] []]
    (-> (create-game/new-game)
        :rows)))

(defexpect configs.game
  ; Game can start with different configs
  (expect
    [{:power 0 :attribute 9001}{:power 1}]
    (-> (create-game/new-game {:hand [{:power 0 :attribute 9001}
                                      {:power 1}]})
        (get-in [:players 0 :hand]))))

