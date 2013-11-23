(ns cblog.controllers.login
   (:require [cblog.models.user :as db-user]
             [noir.response :as resp]
             [noir.session :as session]
             [noir.util.crypt :as crypt]
             [cblog.views.layout :as layout]
             [noir.validation :as vali]
             ))

;; GET "/login"
(defn new []
  (layout/render
    "login.html"
     {
       :email-error (vali/on-error :email first)
       :pass-error (vali/on-error :pass first)
       }))


;; POST "/login"
(defn create [email pass]
  ;; Find user in db
  (let [user (db/find-user-by-email email)]
    ;; Validate user and pass
    (if (valid-authization? user pass)
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



;; DELETE session
(defn destroy []
  (session/clear!)
  (resp/redirect "/"))













