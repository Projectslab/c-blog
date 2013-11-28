(ns cblog.posts
  (:require [ajax.core :refer [GET POST PUT]]
            [domina :refer [by-id styles set-styles!]]
            [domina.events :refer [listen! prevent-default stop-propagation]]
            [domina.css :refer [sel]]))

(defn validate_form []
  (listen! (sel "#new-post-wrapper > form") :submit
           (fn [e]
             (do
               (.log js/console "blah-blah")
               (prevent-default e)
               (stop-propagation e)
               false))))

(defn trigger-new-post-form []
  (let [new-post (sel "#new-post-wrapper")]
    (listen! (by-id "new-post-button") :click
             (fn [e]
               (set-styles! new-post
                            {:display (if (= (:display (styles new-post)) "none")
                                        "block"
                                        "none")})))))


(defn ^:export init []
  (validate_form)
  (trigger-new-post-form))
