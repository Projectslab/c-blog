(ns cblog.handler
  (:require [compojure.core :refer [defroutes]]
            [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [com.postspectacular.rotor :as rotor]
            [cblog.config.routes :refer [cljs-routes home-routes post-routes user-routes session-routes]]
            [cblog.config.schema :as schema]
            [clojure.pprint]))

(defroutes
  app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info,
     :enabled? true,
     :async? false,
     :max-message-per-msecs nil,
     :fn rotor/append})
  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "cblog.log", :max-size (* 512 1024), :backlog 10})
  ;(schema/create-posts-table)
  (selmer.parser/cache-off!)
  (timbre/info "cblog started successfully"))

(defn dev-log [handler]
  (fn [request]
    (let [response (handler request)]
      (timbre/info
        "request:\n"
        (with-out-str (clojure.pprint/pprint request))
        "\nresponse:\n"
        (with-out-str (clojure.pprint/pprint response))
      response))))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "cblog is shutting down..."))

(def app
 (middleware/app-handler
   [cljs-routes user-routes session-routes home-routes app-routes post-routes]
   :middleware
   []
   :access-rules
   []
   :formats
   [:json-kw :edn]))









