(ns cblog.routes.auth
  (:use compojure.core)
  (:require [cblog.views.layout :as layout]
            [noir.session :as session]
            [noir.response :as resp]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]
            [cblog.models.db :as db]))

(defn valid? [name email pass pass1]
  (vali/rule (vali/has-value? name)
             [:name "Name is required"])
  (vali/rule (vali/min-length? pass 5)
             [:pass "password must be at least 5 characters"])
  (vali/rule (= pass pass1)
             [:pass1 "entered passwords do not match"])
  (not (vali/errors? :name :pass :pass1)))

(defn register [& [name]]
  (layout/render
    "registration.html"
    {:name name
     :name-error (vali/on-error :name first)
     :pass-error (vali/on-error :pass first)
     :pass1-error (vali/on-error :pass1 first)}))

(defn handle-registration [name email pass pass1]
  (if (valid? name email pass pass1)
    (try
      (do
        (db/create-user {:name name :email email :pass (crypt/encrypt pass)})
        (let [user (db/find-user-by-email email)]
          (session/put! :user-id (:id user)))
        (resp/redirect "/"))
      (catch Exception ex
        (vali/rule false [:name (.getMessage ex)])
        (register)))
    (register name)))

(defn profile []
  (layout/render
    "profile.html"
    {:user (db/get-user (session/get :user-id))}))

(defn update-profile [{:keys [name email]}]
  (db/update-user (session/get :user-id) name email)
  (profile))

(defn handle-login [email pass]
  (let [user (db/find-user-by-email email)]
    (if (and user (crypt/compare pass (:pass user)))
      (session/put! :user-id (:id user)))
    (resp/redirect "/")))

(defn logout []
  (session/clear!)
  (resp/redirect "/"))

(defroutes auth-routes
  (GET "/register" []
       (register))

  (POST "/register" [name email pass pass1]
        (handle-registration name email pass pass1))

  (GET "/profile" [] (profile))
  
  (POST "/update-profile" {params :params} (update-profile params))
  
  (POST "/login" [email pass]
        (handle-login email pass))

  (GET "/logout" []
        (logout)))
