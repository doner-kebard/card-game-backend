(ns configs)

(def out-of-turn "Out of turn play")

(def no-opp "Waiting for an opponent")

(def play "Playing")

(def wait "Waiting for opponent's play")

(def too-many-players "Too many players")

(defn ini-hand
  "Generates de initial hand (can't be empty)"
  []
  [{:power 5}
   {:power 6}
   {:power 7}
   {:power 8}
   {:power 9}
   {:power 10}
   {:power 11}
   {:power 12}
   {:power 13}
   {:power 14}
   {:power 15}
   {:power 16}]) 
