(ns cblog.config.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/blog"
   :user "postgres"
   :password "gfhjkm"})

;;(defn create-users-table []
;  (sql/with-connection db-spec
;    (sql/create-table
;      :users
;      [:id "varchar(20) PRIMARY KEY"]
;      [:name "varchar(30)"]
;      [:email "varchar(30)"]
;      [:admin :boolean]
;      [:last_login :time]
;      [:is_active :boolean]
;      [:pass "varchar(100)"])))

;(defn create-tables
;  "creates the database tables used by the application"
;  []
;  (create-users-table))


