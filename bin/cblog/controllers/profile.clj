(ns  cblog.controllers.profile
  (:require
    [noir.session :as session]
    [cblog.views.layout :as layout]
    [cblog.models.user :as db-user]))

(defn profile []
  (layout/render
    "profile.html"
    {:user (db-user/get-user (session/get :user-id))}))

(defn update-profile [{:keys [first-name last-name email]}]
  (db-user/update-user (session/get :user-id) first-name last-name email)
  (profile))
