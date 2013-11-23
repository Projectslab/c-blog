(ns cblog.controllers.login
   (:use [schmetterling.core :only (debugger)])
   (:require [cblog.models.user :as db-user]
             [noir.response :as resp]
             [noir.session :as session]
             [noir.util.crypt :as crypt]
             [cblog.views.layout :as layout]
             [noir.validation :as vali]
             ))

(defn valid? [id pass]
  ;; Check if email field is empty
  (vali/rule (vali/has-value? id)
             [:id "email must not be empty"])
  ;; Check if pass field is empty
  (vali/rule (vali/has-value? pass)
             [:pass "password must not be empty"])

  (not (vali/errors? :email :pass)))


;; POST "/login"
(defn handle-login [id pass]
  (debugger)
  ;; Validate id(email) and pass
  (if (valid? id pass)
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
       :email-error (vali/on-error :id first)
       :pass-error (vali/on-error :pass first)
       }))


(defn logout []
  (session/clear!)
  (resp/redirect "/"))









