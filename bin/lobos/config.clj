(ns lobos.config
  (:use lobos.connectivity)
  (:require [cblog.config.db :as db]))



(open-global db/db-spec)
