(ns cblog.controllers.registration
  (:require
    [noir.validation :as vali]
    [noir.session :as session]
    [cblog.views.layout :as layout]
    [noir.response :as resp]
    [cblog.models.user :as db-user]
    [noir.util.crypt :as crypt]
   ))

(defn valid-registration? [myname email pass pass1]
  (vali/rule (vali/has-value? myname)
             [:myname "Name is required"])
  (vali/rule (vali/has-value? email)
             [:email "Email is required"])
  (vali/rule (vali/is-email? email)
             [:email "Incorrect email format"])
  (vali/rule (vali/min-length? pass 5)
             [:pass "password must be at least 5 characters"])
  (vali/rule (= pass pass1)
             [:pass1 "entered passwords do not match"])
  (not (vali/errors? :myname :email :pass :pass1)))


(defn register [& [myname email]]
  (layout/render
    "registration.html"
    {
     :myname myname
     :email email
     :myname-error (vali/on-error :myname first)
     :email-error (vali/on-error :email first)
     :pass-error (vali/on-error :pass first)
     :pass1-error (vali/on-error :pass1 first)}))
(register "Roman" "mail@mail.com")

(defn handle-registration [myname email pass pass1]
  (if (valid-registration? myname email pass pass1)
    (try
      (do
        (db-user/create-user {:myname myname :email email :pass (crypt/encrypt pass)})
        (session/put! :user-id email)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:id (.getMessage ex)])
        (register)))
    (register myname email)))





