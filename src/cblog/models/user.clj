(ns cblog.models.user
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [cblog.config.db :as config]
            [noir.validation :as vali]
            [noir.util.crypt :as crypt]))

;;Get db connection
(defdb db config/db-spec)

;;Define user model, entity
(defentity users)

(defn create-user [user]
  (insert users
          (values user)))

(defn update-user [id first-name last-name email]
  (update users
  (set-fields {:first_name first-name
               :last_name last-name
               :email email})
  (where {:id id})))

(defn find-user [id]
  (first (select users
                 (where {:id id})
                 (limit 1))))


(defn find-user-by-email [email]
  (first (select users
                 (where {:email email})
                 (limit 1))))

;; Validation ;;

(defn validate-login? [user pass]
  (and (vali/rule (vali/not-nil? user)
                  [:email "User with given email not found"])
       (vali/rule (crypt/compare pass (:pass user))
                  [:pass "Password is not correct"]))
  (not (vali/errors? :email :pass)))


(defn validate-registration? [myname email pass pass1]
  (vali/rule (vali/has-value? myname)
             [:myname "Name is required"])
  (vali/rule (vali/has-value? email)
             [:email "Email is required"])
  (vali/rule (vali/is-email? email)
             [:email "Incorrect email format"])
  (vali/rule (vali/min-length? pass 5)
             [:pass "password must be at least 5 characters"])
  (vali/rule (= pass pass1)
             [:pass1 "entered passwords do not match"])
  (not (vali/errors? :myname :email :pass :pass1)))


