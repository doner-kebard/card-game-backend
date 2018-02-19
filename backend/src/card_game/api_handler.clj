(ns card-game.api-handler
  (:use card-game.api
        compojure.core
        cheshire.core
        ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]))

(defn play-action
  [game player action]
  (play-card-as-player game player (:index action) (:row action))
  )

(defroutes app-routes
  (context "/games" [] (defroutes game-list-routes
    (POST "/" [] (create-game))
    (context "/:id" [id] (defroutes game-routes
      (POST "/" [] (add-player id))
      (context "/player/:pid" [pid] (defroutes player-routes
        (GET "/" [] (get-game id pid))
        (POST "/" {body :body} (play-action id pid body))))))))
  (route/not-found "<h1>Page not found</h1>"))

(def entry
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))
