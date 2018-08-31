(ns rules.count-cards-test
  (:require [expectations.clojure.test :refer :all]
            [rules.count-cards :as count-cards]))

(defexpect count.cards

  (def game-state {:cards [{:power 1 :location [:hand] :owner 1}
                           {:power 2 :owner 0 :location [:row 2]}
                           {:power 4 :mess 0 :owner 1}
                           {:power 8 :location [:hand] :owner 1}
                           {:power 16 :location [:row 3] :owner 1}
                           {:power 32 :location [:row 3] :owner 0}]})

  ; Counts correctly
  (expect
    4
    (count-cards/count-cards game-state {:owner 1}))
  
  (expect
    2 
    (count-cards/count-cards game-state {:location [:row 3]}))
  
  (expect
    1
    (count-cards/count-cards game-state {:owner 0 :location [:row 3]}))

  (expect
    2
    (count-cards/count-cards game-state {:location [:hand]}))
  
  ; Uses summand correctly
  (expect
    29
    (count-cards/count-cards game-state {:owner 1} :power))
  
  (expect
    16
    (count-cards/count-cards game-state {:owner 1 :location [:row 3]} :power)))
