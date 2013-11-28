(ns cblog.controllers.posts
   (:require [cblog.models.post :as post-model]
             [noir.response :as resp]
             [noir.session :as session]
             [taoensso.timbre :as timbre]
             [cblog.views.layout :as layout]
             [noir.response :refer [edn]])
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

;; POST /posts
(defn create [title subject]
  (let [timenow (to-long (from-time-zone (local-now) (time-zone-for-offset 0)))
        ;; models function returns info about last inserted row,
        ;; needed it to know weather was inserted data or not, and
        ;; to send id of the last row back to browser
        last-row (post-model/create-post {:title title
                                         :subject subject
                                         :user_id (session/get :user-id)
                                         :created_at timenow})]
    (if-not (nil? last-row)
      (edn {:result "ok" :id (:id last-row)})
      (edn {:result "error"}))))

;; PUT /posts/:id
(defn update [id title subject]
  (let [post-info (post-info id)]
    (layout/render "posts/edit.html"
      (if (check-user (:user_id post-info))
        {:post post-info}
        {:error "You havn't rights to edit this title"}))))

;; DELETE /posts/:id
(defn delete [id]
  (let [post-info (post-info id)]
    (if (check-user (:user_id post-info))
      (post-model/delete-post (read-string id)))
    (resp/redirect "/")))

;; GET/posts/:id
(defn show [id]
  (println "show post"))

;(to-long (from-time-zone (local-now) (time-zone-for-offset 0)))
;(def myformatter (formatters :rfc822))

;(unparse myformatter timenow)


;[clj-time.format :only [formatter formatters unparse ]]











