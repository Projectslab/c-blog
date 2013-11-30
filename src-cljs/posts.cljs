(ns cblog.posts
  (:require [ajax.core :refer [GET POST PUT ajax-request transform-opts]]
            [domina :refer [by-id by-class styles set-styles! value insert-after!]]
            [domina.events :refer [listen! prevent-default stop-propagation]]
            [domina.css :as css]
            [dommy.core :as dommy]
            [dommy.utils :as utils])
  (:use-macros
    [dommy.macros :only [sel sel1 node deftemplate]]))

;; calculated once selectors
(def wrapper
  (css/sel "#new-post-wrapper"))

(def button
  (css/sel "#new-post-button"))

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
  (listen! (-> wrapper (css/sel "form")) :submit
           (fn [e]
             (stop-propagation e)
             (prevent-default e)
             (let [val (fn [x] (value (-> wrapper (css/sel x))))]
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

(deftemplate message [text]
  [:div.message ^:text text])

(defn delete-handler [elem]
  (fn [resp]
    (->> (sel :.message )
         (mapv #(dommy/remove! %)))
    (if (= (:result resp) "ok")
      (do (dommy/insert-before! (message "Статья успешно удалена") elem)
          (dommy/remove! elem))
      (dommy/insert-before! (message (:error resp)) elem))))


;; delete post
(defn delete-post []
  (->> (sel :.delete-post)
       ;; apply listener to all elements in selector
       (mapv #(dommy/listen! % :click
         ;; event hendler
         (fn [e]
           (.preventDefault e)
           ;; get id of post stored in data-id attr of parent's div
           (let [post-div (dommy/closest % :div)
                 id (dommy/attr post-div :data-id)]
             (ajax-request "/posts" "DELETE"
                           (transform-opts {:params {:id id}
                                            :handler (delete-handler post-div)}))))))))

;; function to export
(defn ^:export init []
  (send-post)
  (toggle-form)
  (delete-post))
