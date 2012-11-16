(defproject budabe-graph "0.1.0-SNAPSHOT"
  :description "A super-simple social graph application"
  :url "https://github.com/mwkuster/budabe-graph"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [ring "1.1.6"]
                 [lein-ring "0.7.5"]
                 [cheshire "4.0.4"]
                 [clojurewerkz/neocons "1.1.0-beta1"]]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler budabe-graph.core/handler})
