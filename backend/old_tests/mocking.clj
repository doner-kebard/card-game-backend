(ns mocking
  (:require [persistence.persistence :as persistence]))

(def mock-game (atom nil))

(defn mock-persistence
  [tests]
    (with-redefs
      [persistence/next-id (constantly 0)
       persistence/save-game (fn [a]
                               (reset! mock-game a))
       persistence/fetch-game (fn [x] 
                                @mock-game)]
      (tests))
    (reset! mock-game nil))
