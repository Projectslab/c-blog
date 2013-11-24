(ns cblog.controllers.application
  (:use korma.core)
  (:require [noir.session :as session]
            [cblog.models.user :refer [find-user]]))


(defn current-user []
  (let [user-id (session/get :user-id)]
    (if user-id
      (find-user user-id)
      nil
      )))
