(ns cblog.models.post
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [cblog.config.db :as config]

            ))

;;Get db connection
(defdb db config/db-spec)

;;Define user model, entity
(defentity posts)


(defn create-post [post]
  (insert posts
          (values post)))


(defn get-all-posts []
  )
