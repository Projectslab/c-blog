(ns  cblog.controllers.profile
  (:require
    [noir.session :as session]
    [cblog.views.layout :as layout]
    [noir.util.crypt :as crypt]
    [noir.response :as resp]
    [noir.validation :as vali]
    [cblog.models.user :as db-user]))


;; GET /users/:id
(defn show []
  (layout/render
    "profile.html"
    {:user (db-user/get-user (session/get :user-id))}))

;; GET /users/new
(defn new [& [myname email]]
  (layout/render
    "registration.html"
    {
     :myname myname
     :email email
     :any-error (vali/on-error :any-error first)
     :myname-error (vali/on-error :myname first)
     :email-error (vali/on-error :email first)
     :pass-error (vali/on-error :pass first)
     :pass1-error (vali/on-error :pass1 first)}))


;; POST /users/
(defn create [myname email pass pass1]
  (if (valid-registration? myname email pass pass1)
    (try
      (do
        (db-user/create-user {:name myname :email email :pass (crypt/encrypt pass)})
        (session/put! :user-id email)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:any-error (.getMessage ex)])
        (register)))
    (register myname email)))


;; PUT /users/:id

(defn update [{:keys [first-name last-name email]}]
  (db-user/update-user (session/get :user-id) first-name last-name email)
  (profile))
