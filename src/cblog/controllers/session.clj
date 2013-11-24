(ns cblog.controllers.session
   (:require [cblog.models.user :as user-model]
             [noir.response :as resp]
             [noir.session :as session]
             [noir.util.crypt :as crypt]
             [cblog.views.layout :as layout]
             [noir.validation :as vali]
             [cblog.models.user :refer [validate-login?]]
             ))

;; GET "/session/new"
(defn new []
  (layout/render
    "login.html"
     {
       :email-error (vali/on-error :email first)
       :pass-error (vali/on-error :pass first)
       }))

;; POST "/session"
(defn create [email pass]
  ;; Find user in db
<<<<<<< HEAD
  (let [user (user-model/find-user-by-email email)]
    ;; Validate user and pass
    (if (validate-login? user pass)
=======
  (let [user (db-user/find-user-by-email email)]
    ;; Validate user and pass
    (if (db-user/validate-login? user pass)
>>>>>>> b3ee2e74444d838a118a740ec40ed2c1126dbab9
      ;; If validation passed then put user id in session
      (do
        (session/put! :user-id (:id user))
        (resp/redirect "/"))
      ;; If validation failed then render login page again
      (layout/render
        "login.html"
        {:email email
         :email-error (vali/on-error :email first)
         :pass-error  (vali/on-error :pass first)}))))

;; DELETE "/session"
(defn destroy []
  (session/clear!)
  (resp/redirect "/"))
<<<<<<< HEAD

















=======
>>>>>>> b3ee2e74444d838a118a740ec40ed2c1126dbab9
