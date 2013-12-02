(ns cblog.controllers.posts
   (:require [cblog.models.post :as post-model]
             [noir.response :as resp]
             [noir.session :as session]
             [taoensso.timbre :as timbre]
             [cblog.views.layout :as layout]
             [noir.response :refer [edn]])
  (:use
     [clj-time.local :only[local-now]]
     [clj-time.coerce :only [to-long from-long]]
     [clj-time.format :only [unparse formatter]]))

;; Index ;; GET /
(defn index []
  (layout/render
   "/posts/index.html"
    (try
        {:posts (post-model/get-all-posts)}
        (catch Exception ex
          (timbre/error "unable to find posts" ex)
          {:error "unable to find posts"}))))

;; Show ;; GET /posts/:id

;; Helper function for show
(defn change-time-format [post]
  (let [time-format (formatter "yyyy-MM-dd HH:mm:ss")
       formatted-time (unparse time-format (clj-time.coerce/from-long (:created_at post)))]
  (assoc-in post [:created_at] formatted-time)))


(defn show [id]
  (layout/render
   "/posts/show.html"
   (let [post (post-model/get-post id)
        post-with-formatted-time (change-time-format post)]
    (try
        {:post post-with-formatted-time}
        (catch Exception ex
          (timbre/error "unable to find post" ex)
          {:error "unable to find post"})))))

;; Create ;; POST /posts
(defn create [title subject]
  (let [timenow (to-long (local-now))
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
  (let [post (post-model/get-post id)]
    (if (check-user (:user_id post))
      (if (post-model/update-post (read-string id) title subject)
          (edn {:result "ok"})
          (edn {:error "Error while updating post"}))
      (edn {:error "You don't have permissios to edit this post"}))))

;; DELETE /posts
(defn delete [id]
  (let [post (post-model/get-post id)]
    (if (check-user (:user_id post))
      (do (post-model/delete-post (read-string id))
          (edn {:result "ok"}))
      (edn {:error "You don't have permissios to delete this post"}))))


;(defn get-data [id]
;  (edn (show id)))

;(to-long (from-time-zone (local-now) (time-zone-for-offset 0)))
;(def myformatter (formatters :rfc822))

;(unparse myformatter timenow)


;[clj-time.format :only [formatter formatters unparse ]]



