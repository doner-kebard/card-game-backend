(ns rules.play.next-play
  (:require [expectations.clojure.test :refer :all]
            [rules.play-card :as play-card]))

(defexpect next-play
  ; Stores next-play
  (expect
    {:player 0 :index 0 :row 0 :target nil}
    (-> {:players [{:hand [{} {}]} {:hand [{} {}]}]
         :rows [[]]}
        (play-card/play-card 0 0 0)
        (get-in [:next-play 0])))
  (expect
    {:player 1 :index 2 :row 3 :target nil}
    (-> {:players [{:hand [{} {}]} {:hand [{} {} {}]}]
         :rows [[] [] []]}
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        (play-card/play-card 1 2 3)
        (get-in [:next-play 1])))
  ; Stores nil when a play is expected
  (expect
    [nil nil]
    (-> {:players [{:hand [{} {}]} {:hand [{} {}]}]
         :rows [[]]}
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        :next-play))
  (expect
    nil
    (-> {:players [{:hand [{} {}]} {:hand [{} {}]}]
         :rows [[]]}
        (play-card/play-card 0 0 0)
        (get-in [:next-play 1])))
  (expect
    nil
    (-> {:players [{:hand [{} {}]} {:hand [{} {} {}]}]
         :rows [[] [] []]}
        (play-card/play-card 0 0 0)
        (play-card/play-card 1 1 1)
        (play-card/play-card 1 2 3)
        (get-in [:next-play 0]))))

(defexpect update-only-when-both-play
  ; Doesn't updates game-state until both players played a card
  (expect
    [[]]
    (-> {:players [{:hand [{}]} {:hand []}]
         :rows [[]]}
        (play-card/play-card 0 0 0)
        :rows)))
