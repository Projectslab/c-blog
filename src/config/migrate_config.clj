(ns config.migrate-config
  (:require [cblog.models.schema :as schema]
            [clojure.java.jdbc :as sql]))

(defn schema-version []
  (sql/with-connection schema/db-spec
    (sql/with-query-results res
       ["SELECT version FROM schema_migrations"]
       (or (:version (last res)) 0))))

(defn update-schema-version [version]
  (sql/with-connection schema/db-spec
     (sql/insert-values :schema_migrations [:version] [version])))

(defn migrate-config []
  { :directory "/src/migrations"
    :current-version schema-version
    :update-version update-schema-version })