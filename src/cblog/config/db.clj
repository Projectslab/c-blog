(ns cblog.config.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec
  {:subprotocol "postgresql"
   :subname "//localhost/blog"
   :user "postgres"
   :password "gfhjkm"})
