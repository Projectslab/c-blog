(ns cblog.controllers.application
  (:use korma.core)
  (:require [noir.session :as session]
            [taoensso.timbre :as timbre]
            [cblog.models.user :refer [find-user]]))

;; Find the user or returns nil
(defn current-user []
  (find-user (session/get :user-id)))










