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
                 [com.taoensso/carmine "2.19.1"]
                 [expectations "2.2.0-rc3" :scope "test"]]
  :plugins [[com.jakemccrary/lein-test-refresh "0.22.0"]
            [lein-expectations "0.0.8"]
            [lein-ring "0.12.5"]
            [lein-cloverage "1.1.1"]
            [jonase/eastwood "0.3.5"]
            [venantius/yagni "0.1.7"]]
  :cloverage {:ns-exclude-regex [#"persistence.*" #"api\.handler"]
              :fail-threshold 95}
  :ring {:handler api.handler/entry}
  :target-path "target/%s"
  :profiles {
             :uberjar {:aot :all}
             :pact {
                    :plugins [[au.com.dius/pact-jvm-provider-lein_2.11 "3.2.11" :exclusions [commons-logging]]]
                    :dependencies [[ch.qos.logback/logback-core "1.2.3"]
                                   [ch.qos.logback/logback-classic "1.2.3"]
                                   [org.apache.httpcomponents/httpclient "4.5.8"]]
                    }}
  :pact {
      :service-providers {
          :lobby {
              :protocol "http"
              :host "localhost"
              :port 3000
              :path "/"
              
              :has-pact-with {
                  :cli {
                     :pact-file ~(str
                                  "file://"
                                  (.getAbsolutePath
                                  (clojure.java.io/file
                                    "pacts/cli-lobby.json")))
                     }
                  }
              }
          }
      }
  )
