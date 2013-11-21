(ns cblog.controllers.login
   (:require [cblog.models.user :as db-user]
             [noir.response :as resp]
             [noir.session :as session]
             [cblog.views.layout :as layout]
             [noir.util.crypt :as crypt]))

(defn handle-login [id pass]
  (let [user (db-user/get-user id)]
    (if (and user (crypt/compare pass (:pass user)))
      (session/put! :user-id id))
    (resp/redirect "/")))

(defn new-login []
  (layout/render
    "login.html"))

(defn logout []
  (session/clear!)
  (resp/redirect "/"))
