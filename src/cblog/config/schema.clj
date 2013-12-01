(ns cblog.config.schema
  (:require [clojure.java.jdbc :as sql]
            [cblog.config.db :refer [db-spec]]))


;(defn create-users-table []
;  (sql/with-connection db-spec
;    (sql/create-table
;      :users
;      [:id "SERIAL"]
;      [:name "varchar(30)"]
;      [:email "varchar(30)"]
;      [:admin :boolean]
;      [:last_login :time]
;      [:is_active :boolean]
;      [:pass "varchar(100)"])))

;(defn create-posts-table []
;  (sql/with-connection db-spec
;    (sql/create-table
;      :posts
;      [:id "SERIAL primary key"]
;      [:titile "varchar(300)"]
;      [:subject "text"]
;      [:user_id "integer"]
;      )))



