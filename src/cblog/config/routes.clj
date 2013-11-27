(ns cblog.config.routes
  (:use compojure.core)
  (:require
    [cblog.controllers.session :as session]
    [cblog.controllers.users :as users-ctrl]
    [cblog.controllers.posts :as posts-ctrl]
    [noir.response :as response]
    [cblog.views.layout :as layout]
    [cblog.utils.md :as md]))

;;;;;;;;;;;;;;;;; User routes  ;;;;;;;;;;;;;;;;;;;
(defroutes user-routes

  ;; Show user registration form
  (GET "/users/new" []
       (users-ctrl/unew))

  ;; Create user
  (POST "/users" [myname email pass pass1]
        (users-ctrl/create myname email pass pass1))

  ;; Show user
  (GET "/users/:id" [id] (users-ctrl/show id))

  ;; Update user
  (PUT "/users/:id" [id params] (users-ctrl/update id params)))

;;;;;;;;;;;; Posts routes ;;;;;;;;;;;;;;;;;
(defroutes post-routes

  ;; Create post
  (POST "/posts" [title subject]
          (posts-ctrl/create title subject))
  ;; edit form
  (GET "/posts/edit/:id" [id] (posts-ctrl/edit-form id))

  (GET "/posts/delete/:id" [id] (posts-ctrl/delete id)))

;;;;;;;;;;;;;; Session routes ;;;;;;;;;;;;;;;;

(defroutes session-routes

  ;; New user session form ( login form )
  (GET "/session/new" [] (session/new))

  ;; New session
  (POST "/session" [email pass]
        (session/create email pass))

  ;; Destroy session ( logout )
  (POST "/session/" []
        (session/destroy)))

;;;;;;;;;;;;;;;; CLJS routes ;;;;;;;;;;;;;;;;;;;;;;;

(def messages
  (atom
    [{:message "Hello world"
      :user    "Foo"}
     {:message "Ajax is fun"
      :user    "Bar"}]))

(defroutes cljs-routes
  (GET "/cljsexample" [] (layout/render "cljsexample.html"))
  (GET "/messages" [] (response/edn @messages))
  (POST "/add-message" [message user]
        (response/edn
          (swap! messages conj {:message message :user user}))))

;;;;;;;;;;;;;;;;;  Home routes ;;;;;;;;;;;;;;;;;;;

;; List all posts on index page
(defroutes home-routes
  (GET "/" [] (posts-ctrl/index)))














