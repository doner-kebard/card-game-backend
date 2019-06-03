(ns api.handler
  (:require [api.base :as api]
            [compojure.core :as compojure]
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
    (compojure/POST "/lobby" [] {:body (api/create-game)})
    (compojure/POST "/games/:id{[0-9]+}"
                    [id]
                    {:body (api/add-player (parse-int id))})
    (compojure/GET "/games/:id{[0-9]+}/player/:player"
                   [id player]
                   {:body (api/get-game (parse-int id) player)})
    (compojure/POST "/games/:id{[0-9]+}/player/:player"
                    [id player :as {body :body}]
                    {:body (play-action (parse-int id) player body)})
    (route/not-found {:body {:error "Page not found"}})))

(def ^:private cors-headers
  {"Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Headers" "Content-Type"
   "Access-Control-Allow-Methods" "OPTIONS"})

(defn handler
  [request]
  (try
    (let [response (app-routes request)]
      (assoc
        (if (contains? (:body response) :error)
          (assoc response :status 400)
          response)
        :headers cors-headers))
    (catch Exception e (prn e){:status 500 :headers cors-headers})))

(def entry
  (middleware/wrap-json-response handler))
