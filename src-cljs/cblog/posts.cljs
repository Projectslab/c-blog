(ns cblog.posts
  (:require [ajax.core :refer [GET POST PUT ajax-request transform-opts]]
            [dommy.core :refer [insert-after! insert-before! remove! append! toggle! value listen! closest attr set-text!]])
  (:use-macros
    [dommy.macros :only [sel sel1 node deftemplate]]))


;; Error message !!!
(deftemplate message [text]
  [:div.message ^:text text])


;; Wrapper for new post
(def new-post-form (sel1 :#new-post-wrapper))

;; Button for new post
(def button (sel1 :#new-post-button))

;; Posts wrapper/container
(def posts-wrapper (sel1 :#posts))


;; Toggle new post form
(defn toggle-form []
  (listen! button :click
           (fn [e] (toggle! new-post-form))))

;; Update list of posts, append to the list

(deftemplate post-in-list [title subject id]
  [:div {:data-id id}
   [:h4
    [:a {:href (str "/posts/" id)} ^:text title]]
   [:a.edit-post {:href (str "")} ^:text (str "Edit")]
   ^:text (str " ")
   [:a.delete-post {:href (str "")} ^:text (str "Delete")]])

(defn update-posts-list [title subject id]
  (append! posts-wrapper (post-in-list title subject id)))

;; Create Post

(defn new-handler [title subject]
  (fn [resp]
    (if (= (:result resp) "ok")
      (let [id (:id resp)]
        (insert-after! (message "Посто отправлен") button)
        (update-posts-list title subject id)
        (toggle! new-post-form))
      (insert-after! (message "Пост не отправлен") button))))

(defn error-handler [error]
  (insert-after! (message "При отправке поста произошла ошибка") button))

(defn create []
  (listen! (-> new-post-form (sel1 :form)) :submit
           (fn [e]
             (.preventDefault e)
             (.stopPropagation e)
             (let [val (fn [x] (value (-> new-post-form (sel1 x))))
                   title (val :#title)
                   subject (val :#subject)]
               ;; AJAX-call - create new post
               (POST "/posts" {:params {:title   title
                                        :subject subject}
                               :handler (new-handler title subject)
                               :error-handler error-handler}))
             false)))


;; Edit Post form

(deftemplate update-form-template [title subject id]
  [:div#update
   [:form
    [:input#title.form-control {:type "text" :required "required" :value title}]
    [:textarea#subject.form-control {:required "required"} ^:text (str subject)]
    [:input#id {:type "hidden" :value id}]
    [:input.btn.btn-default {:type "submit" :value "Update"}]]])


(defn edit-resp-handler [resp]
  (append! (sel1 :#posts) (update-form-template (:title resp) (:subject resp) (:id resp))))


(defn edit []
  (let [elem (sel1 :#update)]
    (if-not (nil? elem)
      (remove! elem)))
  (listen! [(sel1 :#posts) :.edit-post] :click
     (fn [e]
       (.preventDefault e)
       (let [post-div (closest (.-selectedTarget e) :div)
             id (attr post-div :data-id)]
         (GET (str "/posts/" id "/data") {:handler edit-resp-handler})))))


;; Update Post action

(defn update-title [id title]
  (->> (sel [:#posts :div])
       (mapv #(if (= (attr % :data-id) id)
               (set-text! (-> % (sel1 [:h4 :a])) title)))))

(defn update-handler [id title]
  (fn [resp]
    (->> (sel :.message)
         (mapv #(remove! %)))
    (if (= (:result resp))
      (do (let [elem (sel1 :#update)]
            (if-not (nil? elem)
              (remove! elem)))
          (update-title id title)))))

(defn update []
  (listen! [(sel1 :#posts) :#update :form] :submit
           (fn [e]
             (.preventDefault e)
             (.stopPropagation e)
             (let [form (.-selectedTarget e)
                   title (value (-> form (sel1 :#title)))
                   subject (value (-> form (sel1 :#subject)))
                   id (value (-> form (sel1 :#id)))]
               (PUT (str "/posts/" id) {:params {:title title
                                                 :subject subject}
                                        :handler (update-handler id title)}))
             false)))

;; Delete Post action

(defn delete-handler [elem]
  (fn [resp]
    (->> (sel :.message )
         (mapv #(remove! %)))
    (if (= (:result resp) "ok")
      (do (insert-before! (message "Статья успешно удалена") elem)
          (remove! elem))
      (insert-before! (message (:error resp)) elem))))

;; delete post
(defn delete []
  (listen! [(sel1 :#posts) :.delete-post] :click
           ;; event hendler
           (fn [e]
             (.preventDefault e)
             ;; get id of post stored in data-id attr of parent's div
             (let [post-div (closest (.-selectedTarget e) :div)
                   id (attr post-div :data-id)]
               (ajax-request "/posts" "DELETE"
                             (transform-opts {:params {:id id}
                                              :handler (delete-handler post-div)}))))))


;; function to export
(defn ^:export init []
  (create)
  (toggle-form)
  (delete)
  (edit)
  (update))
