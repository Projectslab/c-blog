(ns cblog.config.routes
  (:use compojure.core)
  (:require
    [cblog.controllers.session :as session]
    [cblog.controllers.users :as users-ctrl]
    [noir.response :as response]
    [cblog.views.layout :as layout]
    [cblog.utils.md :as md]))

;; User routes
(defroutes user-routes

  ;; Show user registration form
  (GET "/users/new" []
       (users-ctrl/unew))

  ;; Create user
  (POST "/users" [myname email pass pass1]
        (users-ctrl/create myname email pass pass1))

  ;; Show user
  (GET "/users/:id" [id] (users-ctrl/show id))

  ;; Update user
  (PUT "/users/:id" [id params] (users-ctrl/update id params)))

;; Session routes

(defroutes session-routes

  ;; New user session form ( login form )
  (GET "/session/new" [] (session/new))

  ;; New session
  (POST "/session" [id pass]
        (session/create id pass))

  ;; Destroy session ( logout )
  (DELETE "/session/" []
        (session/destroy)))

(def messages
  (atom
    [{:message "Hello world"
      :user    "Foo"}
     {:message "Ajax is fun"
      :user    "Bar"}]))

(defroutes cljs-routes
  (GET "/cljsexample" [] (layout/render "cljsexample.html"))
  (GET "/messages" [] (response/edn @messages))
  (POST "/add-message" [message user]
        (response/edn
          (swap! messages conj {:message message :user user}))))

(defn home-page []
  (layout/render
    "home.html" {:content (md/md->html "/md/docs.md")}))

(defroutes home-routes
  (GET "/" [] (home-page)))


