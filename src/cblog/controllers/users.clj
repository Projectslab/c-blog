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
    "profile.html"
<<<<<<< HEAD
    {:user (user-model/find-user id)}))

;; GET /users/new
(defn unew [& [myname email]]
=======
    {:user (db-user/find-user (session/get :user-id))}))

;; GET /users/new
(defn new-user [& [myname email]]
>>>>>>> b3ee2e74444d838a118a740ec40ed2c1126dbab9
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

<<<<<<< HEAD
(unew "roman" "mail@mail.com")

;; POST /users/
(defn create [myname email pass pass1]
  (if (validate-registration? myname email pass pass1)
=======
;; POST /users/
(defn create [myname email pass pass1]
  (if (db-user/validate-registration? myname email pass pass1)
>>>>>>> b3ee2e74444d838a118a740ec40ed2c1126dbab9
    (try
      (do
        (user-model/create-user {:name myname :email email :pass (crypt/encrypt pass)})
        (session/put! :user-id email)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:any-error (.getMessage ex)])
<<<<<<< HEAD
        (unew)))
    (unew myname email)))


;; PUT /users/:id

(defn update [id {:keys [first-name last-name email]}]
  (user-model/update-user id first-name last-name email)
=======
        (new-user)))
    (new-user myname email)))

;; PUT /users/:id
(defn update [{:keys [first-name last-name email]}]
  (db-user/update-user (session/get :user-id) first-name last-name email)
>>>>>>> b3ee2e74444d838a118a740ec40ed2c1126dbab9
  (show))
