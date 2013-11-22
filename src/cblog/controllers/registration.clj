(ns cblog.controllers.registration
  (:require
    [noir.validation :as vali]
    [noir.session :as session]
    [cblog.views.layout :as layout]
    [noir.response :as resp]
    [cblog.models.user :as db-user]
    [noir.util.crypt :as crypt]
   ))

(defn valid? [id pass pass1]
  (vali/rule (vali/has-value? id)
             [:id "user ID is required"])
  (vali/rule (vali/min-length? pass 5)
             [:pass "password must be at least 5 characters"])
  (vali/rule (= pass pass1)
             [:pass1 "entered passwords do not match"])
  (not (vali/errors? :id :pass :pass1)))


(defn register [& [id]]
  (layout/render
    "registration.html"
    {:id id
     :id-error (vali/on-error :id first)
     :pass-error (vali/on-error :pass first)
     :pass1-error (vali/on-error :pass1 first)}))

(defn handle-registration [id pass pass1]
  (if (valid? id pass pass1)
    (try
      (do
        (db-user/create-user {:id id :pass (crypt/encrypt pass)})
        (session/put! :user-id id)
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:id (.getMessage ex)])
        (register)))
    (register id)))



