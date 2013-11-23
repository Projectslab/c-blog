(ns cblog.routes.auth
  (:use compojure.core)
  (:require
            [cblog.controllers.login :as login]
            [cblog.controllers.registration :as reg]
            [cblog.controllers.profile :as profile])
  )

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
