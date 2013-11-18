(defproject cblog "0.1.0-SNAPSHOT"
  :dependencies [[ring-server "0.3.1"]
                 [domina "1.0.2"]
                 [org.clojure/clojurescript "0.0-2030"]
                 [markdown-clj "0.9.34"]
                 [com.taoensso/timbre "2.7.1"]
                 [prismatic/dommy "0.1.2"]
                 [korma "0.3.0-RC6"]
                 [selmer "0.5.2"]
                 [org.clojure/clojure "1.5.1"]
                 [cljs-ajax "0.2.2"]
                 [log4j "1.2.17" :exclusions
                  [javax.mail/mail
                   javax.jms/jms
                   com.sun.jdmk/jmxtools
                   com.sun.jmx/jmxri]]
                 [compojure "1.1.6"]
                 [com.taoensso/tower "1.7.1"]
                 [lib-noir "0.7.5"]
                 [com.postspectacular/rotor "0.1.0"]
                 [postgresql/postgresql "9.1-901.jdbc4"]]
  :cljsbuild
  {:builds [{:source-paths ["src-cljs"]
             :compiler {:pretty-print false
                        :output-to "resources/public/js/site.js"
                        :optimizations :advanced}}]}
  :ring {:handler cblog.handler/app
         :init    cblog.handler/init
         :destroy cblog.handler/destroy}
  :profiles
  {:production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.1"]]}}
  :url "http://example.com/FIXME"
  :aot
  :all
  :plugins [[lein-ring "0.8.7"]
            [lein-cljsbuild "0.3.3"]]
  :description "Simple collective blog with authorisation"
  :min-lein-version "2.0.0")
