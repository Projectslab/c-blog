(ns cblog.views.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [noir.session :as session]
            [compojure.response :refer [Renderable]]))

(def template-path "cblog/views/templates/")


(deftype
  RenderableTemplate
  [template params]
  Renderable
  (render
    [this request]
    (content-type
      (->>
        (assoc
          params
          (keyword (s/replace template #".html" "-selected"))
          "active"
          :servlet-context
          (:context request)
          :user-id
          (session/get :user-id))
        (parser/render-file (str template-path template))
        response)
      "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))




