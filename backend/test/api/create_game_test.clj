(ns api.create-game-test
  (:require [expectations.clojure.test :refer :all]
            [clojure.test :as ctest]
            [mocking :as mocking]
            [api.base :as base]
            [configs.messages :as messages]))

(ctest/use-fixtures :each mocking/mock-persistence)

(defexpect game-creation
  (expect 0 (:game-id (base/create-game)))
  (expect messages/no-opp (:status (base/create-game)))
  (expect nil (:player-ids (base/create-game)))
  (expect true (contains? (base/create-game) :player-id)))
