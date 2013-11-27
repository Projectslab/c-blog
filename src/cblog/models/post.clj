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


(defn get-all-posts []
  (select posts
        (fields :id :title :subject :created_at))
)

;(select posts
;        (fields :id :title :subject :created_at))


