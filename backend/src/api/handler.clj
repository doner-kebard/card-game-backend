(ns api.handler
  (:require [api.base :as api])
  (:use compojure.core
        cheshire.core
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.coercions :as coerce]))

(defn ^:private parse-int [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception e nil)))

(defn ^:private play-action
  [game-id player-id action]
    (api/play-card-as-player
    game-id
    player-id
    (parse-int (:index action))
    (parse-int (:row action))))

(defroutes ^:private app-routes
  (POST "/games" [] (api/create-game))
  (POST "/games/:id{[0-9]+}"
        [id :<< coerce/as-int]
        (api/add-player id))
  (GET "/games/:id{[0-9]+}/player/:player"
       [id :<< coerce/as-int player]
       (api/get-game id player))
  (POST "/games/:id{[0-9]+}/player/:player"
        [id :<< coerce/as-int player :as {body :body}]
        (play-action id player body))
  (route/not-found "<h1>Page not found</h1>"))

(def ^:private cors-headers 
  { "Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "OPTIONS"})

(defn ^:private responsify
  [handler]
  (fn [request]
    (update-in (response (handler request))
               [:headers]
               merge cors-headers)))

(def entry
  (-> (responsify app-routes)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))
