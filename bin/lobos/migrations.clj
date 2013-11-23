(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
               config helpers)))

(defmigration add-posts-table
  (up [] (create
          (tbl :posts
            (varchar :title 200 :unique)
            (text :content)
            (refer-to :users))))
  (down [] (drop (table :posts))))
