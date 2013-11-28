(ns cblog.posts
  (:require [ajax.core :refer [GET POST PUT]]
            [domina :refer [by-id]]
            [domina.events :refer [listen! prevent-default stop-propagation]]))

(defn validate_form []
  (listen! (by-id "new-post") :submit
           (fn [e]
             (do
               (.log js/console "blah-blah")
               (prevent-default e)
               (stop-propagation e)
               false))))

(defn ^:export init []
  (validate_form))
