(ns cblog.controllers.posts
   (:require [cblog.models.post :as post-model]
             [noir.response :as resp]
             [noir.session :as session]
             [cblog.views.layout :as layout]
             )
  (:use
     [clj-time.local :only[local-now]]
     [clj-time.core :only [from-time-zone time-zone-for-offset default-time-zone ]]))

;; GET /
(defn index []
  (layout/render
   "/posts/index.html"
    (try
        {:posts (post-model/get-all-posts)}
        (catch Exception ex
          (timbre/error "unable to find users" ex)
          {:error "unable to find users"}))))



;; POST /posts/
(defn create [title subject]
  (let [timenow (from-time-zone (local-now) (time-zone-for-offset 0))]
   (post-model/create-post {:title title
                            :subject subject
                            :user_id (session/get :user-id)
                            :created_at timenow})
   (resp/redirect "/")))



;(def myformatter (formatters :rfc822))

;(unparse myformatter timenow)


;[clj-time.format :only [formatter formatters unparse ]]








