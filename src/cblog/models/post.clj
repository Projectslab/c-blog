(ns cblog.models.post
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [cblog.config.db :as config]
            ))

;;Get db connection
(defdb db config/db-spec)

;;Define user model, entity
(defentity user)

;;Define user model, entity
(defentity posts
  (has-one user))


(defn create-post [post]
  (insert posts
          (values post)))

(defn get-all-users-post [user-id]
  (select posts
          (fields :id :title :subject :created_at)
          (where {:user_id user-id})))

(defn get-all-posts []
  (select posts
        (fields :id :title :subject :created_at)))

(defn get-post [id]
  (first (select posts
                 (fields :id :title :subject :created_at :user_id)
                 (where {:id (read-string id)}))))
(get-post "3")

(defn delete-post [id]
  (delete posts
          (where {:id id})))

(defn update-post [id title subject]
  (update posts
          (set-fields {:title title
                       :subject subject})
          (where {:id id})))

;(select posts (fields :id :title :subject :created_at))
;(get-all-posts)




