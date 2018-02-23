(ns card-game.api-handler
  (:require [card-game.api :as api])
  (:use compojure.core
        cheshire.core
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]))

(defn play-action
  [game player action]
  (api/play-card-as-player game player (:index action) (:row action))
  )

(defroutes app-routes
  (context "/games" [] (defroutes game-list-routes
    (POST "/" [] (api/create-game))
    (context "/:id" [id] (defroutes game-routes
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
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
