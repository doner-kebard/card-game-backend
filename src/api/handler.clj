(ns api.handler
  (:require [api.base :as api])
  (:require [compojure.core :as compojure]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]))

(defn ^:private parse-int [number-string]
  (try (Long/parseLong number-string)
       (catch Exception e nil)))

(defn ^:private play-action
  [game-id player-id action]
  (api/play-card-as-player
    game-id
    player-id
    (parse-int (:index action))
    (parse-int (:row action))))

(def^:private app-routes
  (compojure/routes
    (compojure/POST "/lobby" [] (api/create-game))
    (compojure/POST "/games/:id{[0-9]+}"
                    [id]
                    (api/add-player (parse-int id)))
    (compojure/GET "/games/:id{[0-9]+}/player/:player"
                   [id player]
                   (api/get-game (parse-int id) player))
    (compojure/POST "/games/:id{[0-9]+}/player/:player"
                    [id player :as {body :body}]
                    (play-action (parse-int id) player body))
    (route/not-found "<h1>Page not found</h1>")))

(defn ^:private handler
  [request]
  {:body (app-routes request)})

(def entry
  (middleware/wrap-json-response handler))
