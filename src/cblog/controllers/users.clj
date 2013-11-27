(ns  cblog.controllers.users
  (:require
    [noir.session :as session]
    [cblog.views.layout :as layout]
    [noir.util.crypt :as crypt]
    [noir.response :as resp]
    [noir.validation :as vali]
    [cblog.models.user :as user-model]
    [taoensso.timbre :as timbre]
    [cblog.controllers.application :refer[current-user]]
    [cblog.models.user :refer [validate-registration?]]))


;; GET /users/:id
(defn show [id]
  (layout/render
    "users/show.html"
    (try
      {:user-info (user-model/find-user (Integer/parseInt id))
       :current-user (current-user)}
      (catch Exception ex
        (timbre/error "unable to find user" ex)
        {:error "user not found"}))))


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
        ;; put newly created user in local var
        (let [new-user (user-model/create-user {:name myname :email email :pass (crypt/encrypt pass)})]
          (session/put! :user-id (:id new-user)))
        (resp/redirect "/")
      (catch Exception ex
        (vali/rule false [:any-error (.getMessage ex)])
        (unew)))
    (unew myname email)))


;; PUT /users/:id

(defn update [id {:keys [first-name last-name email]}]
  (user-model/update-user id first-name last-name email)
  (show id))
