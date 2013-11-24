(ns cblog.controllers.application
  (:use korma.core)
  (:require [noir.session :as session]
            [cblog.models.user :refer [find-user]]))


(defn current-user []
  ;; Get user id from session
  (let [user-id (session/get :user-id)]
    ;; If user id is there
    (if user-id
      ;; Returns user map {:id :name ...}
      (find-user user-id)
      ;; If no user id return nil
      nil
      )))
