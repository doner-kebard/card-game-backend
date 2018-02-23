(defproject card-game "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [cheshire "5.8.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-json "0.1.2"]
                 [com.taoensso/carmine "2.17.0"]
                 [expectations "2.2.0-rc3" :scope "test"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.22.0"]
            [lein-expectations "0.0.8"]
            [lein-ring "0.12.1"]]
  :ring {:handler card-game.api-handler/entry}
  :main ^:skip-aot card-game.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
