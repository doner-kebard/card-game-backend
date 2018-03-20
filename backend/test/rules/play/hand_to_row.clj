(ns rules.play.hand-to-row
  (:require [expectations.clojure.test :refer :all]
            [rules.play-card :as play-card]))

(defexpect move-card
  ; Playing a card makes the card dissappear from the hand
  (expect
    [{:hand []} {:hand [{:blep "S"}]}]
    (-> {:players [{:hand [{}]}
                   {:hand [{:blep "S"} {}]}]
         :rows [[] []]}
        (play-card/play-card 0 0 1)
        (play-card/play-card 1 1 0)
        :players))

  ; Playing a card makes the card appear in play with an owner
  (expect
    [[{:blep "S" :owner 1}] [{:boop 1 :owner 0}]]
    (-> {:players [{:hand [{:boop 1}]}
                   {:hand [{} {:blep "S"}]}]
         :rows [[] []]}
        (play-card/play-card 0 0 1)
        (play-card/play-card 1 1 0)
        :rows)))
