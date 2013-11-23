(ns cblog.controllers.login
   (:use [schmetterling.core :only (debugger)])
   (:require [cblog.models.user :as db-user]
             [noir.response :as resp]
             [noir.session :as session]
             [noir.util.crypt :as crypt]
             [cblog.views.layout :as layout]
             [noir.validation :as vali]
             ))


(defn valid-authization? [user pass]
  (and (vali/rule (vali/not-nil? user)
                  [:email "User with given email not found"])
       (vali/rule (crypt/compare pass (:pass user))
                  [:pass "Password is not correct"]))
  (not (vali/errors? :email :pass)))


;; POST "/login"
(defn handle-login [id pass]
  ;; Validate id(email) and pass
  (if (valid-authization? id pass)
    ;; If email and pass not empty check if user exists
     (let [user (db-user/get-user id)]
      ;; Check if pass correct
      (if (and user (crypt/compare pass (:pass user)))
        ;; If pass correct set session
        (do (session/put! :user-id id)
          ;; Redirect to main page
            (resp/redirect "/"))
        ;; If not
        ;; TODO show error message
        (resp/redirect "/login")))
     ;; If not valid show login and errors
     (resp/redirect "/login")))

;; GET "/login"
(defn new-login []
  (layout/render
    "login.html"
     {
       :email-error (vali/on-error :email first)
       :pass-error (vali/on-error :pass first)
       }))


(defn logout []
  (session/clear!)
  (resp/redirect "/"))













