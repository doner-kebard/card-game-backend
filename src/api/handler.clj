(ns api.handler
  (:require [api.base :as api])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :as compojure]
            [cheshire.core :as cheshire]
            [ring.util.response :as response]
            [ring.middleware.json :as middleware]
            [compojure.coercions :as coerce]))

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
    (compojure/POST "/games" [] (api/create-game))
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

(def ^:private cors-headers 
  { "Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "OPTIONS"})

(defn ^:private responsify
  [handler]
  (fn [request]
    (update-in (response/response (handler request))
               [:headers]
               merge cors-headers)))

(def entry
  (-> (responsify app-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))
