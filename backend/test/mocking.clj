(ns mocking
  (:require [persistence.persistence :as persistence]))

(def mock-game (atom {}))

(def mock-id (atom 0))

(defn mock-persistence
  [tests]
    (with-redefs
      [persistence/next-id (fn []
                             (do
                               (reset! mock-id (inc @mock-id))
                               @mock-id))
       persistence/save-game (fn [game]
                               (reset!
                                 mock-game
                                 (assoc
                                   @mock-game
                                   {:game-id (:game-id game)}
                                   game))
                               game)
       persistence/fetch-game (fn [id]
                                (@mock-game {:game-id id}))]
      (tests)))
