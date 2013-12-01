(ns cblog.controllers.posts
   (:require [cblog.models.post :as post-model]
             [noir.response :as resp]
             [noir.session :as session]
             [taoensso.timbre :as timbre]
             [cblog.views.layout :as layout]
             [noir.response :refer [edn]])
  (:use
     [clj-time.local :only[local-now]]
     [clj-time.coerce :only [to-long from-long]]))

;; Index ;; GET /
(defn index []
  (layout/render
   "/posts/index.html"
    (try
        {:posts (post-model/get-all-posts)}
        (catch Exception ex
          (timbre/error "unable to find users" ex)
          {:error "unable to find users"}))))


;; Show ;; GET /posts/:id
(defn show [id]
  (let [post (post-model/get-post (read-string id))]
    ))


;; Create ;; POST /posts
(defn create [title subject]
  (let [timenow (local-now)
        ;; returns model that last inserted to db
        last-row (post-model/create-post {:title title
                                         :subject subject
                                         :user_id (session/get :user-id)
                                         :created_at timenow})]
    (if-not (nil? last-row)
      (edn {:result "ok" :id (:id last-row)})
      (edn {:result "error"}))))



(defn check-user [user-id]
  (= (session/get :user-id) user-id))


;;Update ;; PUT /posts/:id
(defn update [id title subject]
  (let [show (show id)]
    (if (check-user (:user_id show))
      (if (post-model/update-post (read-string id) title subject)
          (edn {:result "ok"})
          (edn {:error "Error while updating post"}))
      (edn {:error "You don't have permissios to edit this post"}))))

;; DELETE /posts
(defn delete [id]
  (let [show (show id)]
    (if (check-user (:user_id show))
      (do (post-model/delete-post (read-string id))
          (edn {:result "ok"}))
      (edn {:error "You don't have permissios to delete this post"}))))


(defn get-data [id]
  (edn (show id)))

;(to-long (from-time-zone (local-now) (time-zone-for-offset 0)))
;(def myformatter (formatters :rfc822))

;(unparse myformatter timenow)


;[clj-time.format :only [formatter formatters unparse ]]

