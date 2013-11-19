(ns cblog.models.schema
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/cblog"
   :user "cblog"
   :password "cblog"})

(defn initialized? [] true)

(defn create-users-table []
  (sql/with-connection db-spec
    (sql/create-table
      :users
      [:id "varchar(20) PRIMARY KEY"]
      [:first_name "varchar(35)"]
      [:last_name "varchar(35)"]
      [:email "varchar(320)"]  ; in accordance with RFC 3696
      [:admin :boolean]
      [:last_login :time]
      [:is_active :boolean]
      [:pass "varchar(100)"]
      [:salt "varchar(20)"])))

(defn create-tables
  "creates the database tables used by the application"
  []
  (create-users-table))
