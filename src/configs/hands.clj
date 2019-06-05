(ns configs.hands)

(def default-hand
  "Default hand (can't be empty)"
  [:8
   :7
   :6-overpower
   :5-overpower
   :6-strengthen
   :6-weaken
   :4-strengthen
   :4-weaken])

(def default-hands
  "Default hands"
  [default-hand default-hand])
