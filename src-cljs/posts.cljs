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

(def posts-wrapper (sel1 :#posts))

(deftemplate post-in-list [title subject id]
  [:div {:data-id id}
   [:h4
    [:a {:href (str "/posts/" id)} ^:text title]]
   [:a.edit-post {:href (str "")} ^:text (str "Edit")]
   ^:text (str " ")
   [:a.delete-post {:href (str "")} ^:text (str "Delete")]])

;; response handlers
(defn new-handler [title subject]
  (fn [resp]
    (if (= (:result resp) "ok")
      (let [id (:id resp)]
        (insert-after! button "<div>Пост отправлен</div>")
        (dommy/append! posts-wrapper (post-in-list title subject id)))
      (insert-after! (by-id "new-post-button") "<div>Пост Не отправлен</div>"))))

(defn error-handler [error]
  (insert-after! (by-id "new-post-button") "<div>При отправке поста произошла ошибка</div>"))

;; send new post
(defn send-post []
  (listen! (-> wrapper (css/sel "form")) :submit
           (fn [e]
             (stop-propagation e)
             (prevent-default e)
             (let [val (fn [x] (value (-> wrapper (css/sel x))))
                   title (val "#title")
                   subject (val "#subject")]
               ;; AJAX-call - create new post
               (POST "/posts" {:params {:title   title
                                        :subject subject}
                               :handler (new-handler title subject)
                               :error-handler error-handler}))
             false)))

;; toggle new post form by clicking button
(defn toggle-form []
  (let [new-post wrapper]
    (dommy/listen! (by-id "new-post-button") :click
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
  (dommy/listen! [(sel1 :#posts) :.delete-post] :click
                 ;; event hendler
                 (fn [e]
                   (.preventDefault e)
                   (js/console.log (.-selectedTarget e))
                   (js/console.log (dommy/closest (.-selectedTarget e) :div))
                   ;; get id of post stored in data-id attr of parent's div
                   (let [post-div (dommy/closest (.-selectedTarget e) :div)
                         id (dommy/attr post-div :data-id)]
                     (ajax-request "/posts" "DELETE"
                                   (transform-opts {:params {:id id}
                                                    :handler (delete-handler post-div)}))))))

;; update form template
(deftemplate update-form [title subject id]
  [:div#update
   [:form
    [:input#title.form-control {:type "text" :required "required" :value title}]
    [:textarea#subject.form-control {:required "required"} ^:text (str subject)]
    [:input#id {:type "hidden" :value id}]
    [:input.btn.btn-default {:type "submit" :value "Update"}]]])

;; get info handler
(defn get-handler [resp]
  (dommy/append! (sel1 :#posts) (update-form (:title resp) (:subject resp) (:id resp)))
  )

;; edit click listener
(defn click-edit []
  (let [elem (sel1 :#update)]
    (if-not (nil? elem)
      (dommy/remove! elem)))
  (dommy/listen! [(sel1 :#posts) :.edit-post] :click
     (fn [e]
       (.preventDefault e)
       (let [post-div (dommy/closest (.-selectedTarget e) :div)
             id (dommy/attr post-div :data-id)]
         (GET (str "/posts/" id "/data") {:handler get-handler})))))

(defn update-handler [id]
  (fn [resp]
    (->> (sel :.message)
         (mapv #(dommy/remove! %)))
    (if (= (:result resp))
      (do (let [elem (sel1 :#update)]
            (js/console.log elem)
            (if-not (nil? elem)
              (dommy/remove! elem)))
          (js/console.log (sel1 [:#posts :div {:data-id id}]))))))

(defn update-post []
  (dommy/listen! [(sel1 :#posts) :#update :form] :submit
                 (fn [e]
                   (.preventDefault e)
                   (.stopPropagation e)
                   (let [form (.-selectedTarget e)
                         title (dommy/value (-> form (sel1 :#title)))
                         subject (dommy/value (-> form (sel1 :#subject)))
                         id (dommy/value (-> form (sel1 :#id)))]
                     (js/console.log form title subject id)
                     (PUT (str "/posts/" id) {:params {:title title
                                                       :subject subject}
                                              :handler (update-handler id)}))
                   false)))

;; function to export
(defn ^:export init []
  (send-post)
  (toggle-form)
  (delete-post)
  (click-edit)
  (update-post))
