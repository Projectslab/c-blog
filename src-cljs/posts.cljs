(ns cblog.posts
  (:require [ajax.core :refer [GET POST PUT]]
            [domina :refer [by-id styles set-styles! value insert-after!]]
            [domina.events :refer [listen! prevent-default stop-propagation]]
            [domina.css :refer [sel]]))

;; calculated once selectors
(def wrapper
  (sel "#new-post-wrapper"))

(def button
  (sel "#new-post-button"))

;; response handlers
(defn handler [resp]
  (if (= (:result resp) "ok")
    (let [id (:id resp)]
      (insert-after! button "<div>Пост отправлен</div>")
      #_( TODO: add new post in my post list ))
    (insert-after! (by-id "new-post-button") "<div>Пост Не отправлен</div>")))

(defn error-handler [error]
  (insert-after! (by-id "new-post-button") "<div>При отправке поста произошла ошибка</div>"))

;; send new post
(defn send-post []
  (listen! (-> wrapper (sel "form")) :submit
           (fn [e]
             (stop-propagation e)
             (prevent-default e)
             (let [val (fn [x] (value (-> wrapper (sel x))))]
               ;; AJAX-call - create new post
               (POST "/posts" {:params {:title   (val "#title")
                                        :subject (val "#subject")}
                               :handler handler
                               :error-handler error-handler}))
             false)))

;; toggle new post form by clicking button
(defn toggle-form []
  (let [new-post wrapper]
    (listen! (by-id "new-post-button") :click
             (fn [e]
               (set-styles! new-post
                            {:display (if (= (:display (styles new-post)) "none")
                                        "block"
                                        "none")})))))

;; function to export
(defn ^:export init []
  (send-post)
  (toggle-form))
