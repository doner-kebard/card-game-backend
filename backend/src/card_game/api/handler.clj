(ns card-game.api.handler
  (:require [card-game.api.base :as api])
  (:use compojure.core
        cheshire.core
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [compojure.coercions :as coerce]))

(defn parse-int [number-string]
  (try (Integer/parseInt number-string)
       (catch Exception e nil)))

(defn play-action
  [game-id player-id action]
    (api/play-card-as-player
    game-id
    player-id
    (parse-int (:index action))
    (parse-int (:row action))))

(defroutes app-routes
  (context "/games" [] (defroutes game-list-routes
    (POST "/" [] (api/create-game))
    (context "/:id" [id :<< coerce/as-int] (defroutes game-routes
      (POST "/" [] (api/add-player id))
      (context "/player/:pid" [pid] (defroutes player-routes
        (GET "/" [] (api/get-game id pid))
        (POST "/" {body :body} (play-action id pid body))))))))
  (route/not-found "<h1>Page not found</h1>"))

(def cors-headers 
  { "Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "OPTIONS"})

(defn responsify
  [handler]
  (fn [request]
    (update-in (response (handler request))
               [:headers]
               merge cors-headers)))

(def entry
  (-> (responsify app-routes)
      (handler/api)
      (middleware/wrap-json-body {:keywords? true})
      (middleware/wrap-json-response)))
