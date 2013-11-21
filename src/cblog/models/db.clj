(ns cblog.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [cblog.models.schema :as schema]))

(defdb db schema/db-spec)

(defentity users)

(defn create-user [user]
  (insert users
          (values user)))

(defn update-user [id name email]
  (update users
  (set-fields {:name name
               :email email})
  (where {:id id})))

(defn get-user [id]
  (first (select users
                 (where {:id id})
                 (limit 1))))
