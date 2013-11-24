(ns  cblog.controllers.users
  (:require
    [noir.session :as session]
    [cblog.views.layout :as layout]
    [noir.util.crypt :as crypt]
    [noir.response :as resp]
    [noir.validation :as vali]
    [cblog.models.user :as user-model]
    [cblog.models.user :refer [validate-registration?]]))


;; GET /users/:id
(defn show [id]
  (layout/render
    "users/show.html"
    {:user-info (user-model/find-user id)}))

;; GET /users/new
(defn unew [& [myname email]]
  (layout/render
    "users/new.html"
    {
     :myname myname
     :email email
     :any-error (vali/on-error :any-error first)
     :myname-error (vali/on-error :myname first)
     :email-error (vali/on-error :email first)
     :pass-error (vali/on-error :pass first)
     :pass1-error (vali/on-error :pass1 first)
     }))


;; POST /users/
(defn create [myname email pass pass1]
  (if (validate-registration? myname email pass pass1)
    (try
      (do
        (user-model/create-user {:name myname :email email :pass (crypt/encrypt pass)})
        (session/put! :user-id email)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:any-error (.getMessage ex)])
        (unew)))
    (unew myname email)))


;; PUT /users/:id

(defn update [id {:keys [first-name last-name email]}]
  (user-model/update-user id first-name last-name email)
  (show))
