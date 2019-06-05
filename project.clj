(defproject card-game "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [cheshire "5.8.1"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-json "0.4.0"]
                 [com.taoensso/nippy "2.14.0"]
                 [ring/ring-mock "0.4.0"]
                 [expectations "2.2.0-rc3" :scope "test"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.22.0"]
            [lein-expectations "0.0.8"]
            [lein-ring "0.12.5"]
            [lein-cloverage "1.1.1"]
            [jonase/eastwood "0.3.5"]
            [venantius/yagni "0.1.7"]]
  :cloverage {:fail-threshold 95}
  :eastwood {:add-linters [:unused-fn-args :unused-locals :unused-namespaces :unused-private-vars]}
  :ring {:handler api.handler/entry}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  )
