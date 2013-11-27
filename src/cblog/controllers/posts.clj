(ns cblog.controllers.posts
   (:require [cblog.models.post :as post-model]
             [noir.response :as resp]
             [noir.session :as session]
             [taoensso.timbre :as timbre]
             [cblog.views.layout :as layout]
             )
  (:use
     [clj-time.local :only[local-now]]
     [clj-time.core :only [from-time-zone time-zone-for-offset default-time-zone ]]
     [clj-time.coerce :only [to-long]]))

;; GET /
(defn index []
  (layout/render
   "/posts/index.html"
    (try
        {:posts (post-model/get-all-posts)}
        (catch Exception ex
          (timbre/error "unable to find users" ex)
          {:error "unable to find users"}))))
;(empty? (post-model/get-all-posts))

(defn post-info [id]
  (post-model/get-post (read-string id)))

(defn check-user [user-id]
  (= (session/get :user-id) user-id))

;; POST /posts/
(defn create [title subject]
  (let [timenow (to-long (from-time-zone (local-now) (time-zone-for-offset 0)))]
   (post-model/create-post {:title title
                            :subject subject
                            :user_id (session/get :user-id)
                            :created_at timenow})
   (resp/redirect "/")))

(defn edit-form [id]
  (let [post-info (post-info id)]
    (layout/render "posts/edit.html"
      (if (check-user (:user_id post-info))
        {:post post-info}
        {:error "You havn't rights to edit this title"}))))

(defn delete [id]
  (let [post-info (post-info id)]
    (if (check-user (:user_id post-info))
      (post-model/delete-post (read-string id)))
    (resp/redirect "/")))
;(to-long (from-time-zone (local-now) (time-zone-for-offset 0)))
;(def myformatter (formatters :rfc822))

;(unparse myformatter timenow)


;[clj-time.format :only [formatter formatters unparse ]]











