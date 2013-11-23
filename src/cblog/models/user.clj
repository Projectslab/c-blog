(ns cblog.models.user
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [cblog.config.db :as config]))

;;Get db connection
(defdb db config/db-spec)

;;Define user model, entity
(defentity users)

(defn create-user [user]
  (insert users
          (values user)))

(defn update-user [id first-name last-name email]
  (update users
  (set-fields {:first_name first-name
               :last_name last-name
               :email email})
  (where {:id id})))

(defn get-user [id]
  (first (select users
                 (where {:id id})
                 (limit 1))))

