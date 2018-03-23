(ns rules.play.messages
  (:require [expectations.clojure.test :refer :all]
            [configs.messages :as messages]
            [rules.play-card :as play-card]))

(defexpect out-of-turn
  ; Can't play a card when you were not supposed to
  (expect
    {:error messages/out-of-turn}
    (-> {:players [{:hand [{} {}]} {:hand [{} {}]}]
         :rows [[]]}
        (play-card/play-card 0 0 0)
        (play-card/play-card 0 0 0))))

(defexpect row-limit
  ; Can't play a card over the limit
  (expect
    {:error messages/row-limit}
    (play-card/play-card {:next-play [nil nil]
                          :rows [{:limit 0 :cards []}]}
                         0 0 0))
  (expect
    {:error messages/row-limit}
    (play-card/play-card {:next-play [nil nil]
                          :rows [{:limit 1 :cards [{:owner 0}]}]}
                         0 0 0)))
