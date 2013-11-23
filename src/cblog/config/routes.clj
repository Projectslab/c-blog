(ns cblog.config.routes
  (:use compojure.core)
  (:require
    [cblog.controllers.login :as login]
    [cblog.controllers.registration :as reg]
    [cblog.controllers.profile :as profile]
    [noir.response :as response]
    [cblog.views.layout :as layout]
    [cblog.util :as util]))

(defroutes auth-routes
  (GET "/register" []
       (reg/register))

  (POST "/register" [myname email pass pass1]
        (reg/handle-registration myname email pass pass1))

  (GET "/profile" [] (profile/profile))

  (POST "/update-profile" {params :params} (profile/update-profile params))

  (GET "/login" [] (login/new-login))

  (POST "/login" [id pass]
        (login/handle-login id pass))

  (GET "/logout" []
        (login/logout)))

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
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))

