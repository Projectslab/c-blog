(defproject cblog "0.1.0-SNAPSHOT"
  :description "Simple collective blog with authorisation"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.7.5"]
                 [compojure "1.1.6"]
                 [ring-server "0.3.1"]
                 [selmer "0.5.2"]
                 [com.taoensso/timbre "2.7.1"]
                 [com.postspectacular/rotor "0.1.0"]
                 [com.taoensso/tower "1.7.1"]
                 [markdown-clj "0.9.35"]
                 [com.cemerick/friend "0.2.0"]
                 [korma "0.3.0-RC6"]]
  :aot :all
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler cblog.handler/app
         :init    cblog.handler/init
         :destroy cblog.handler/destroy}
  :profiles
  {:production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.1"]]}}
  :min-lein-version "2.0.0")