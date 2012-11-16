(ns budabe-graph.core
  (:use [compojure.core :only (GET PUT POST defroutes)])
  (:require [compojure.route :as route]
            [ring.util.response :as response]
            [cheshire.core :as json]))

(defrecord User [id, username, first_name, last_name,  gender, country])

(defrecord Relationship [id, type, from_user, to_user])

(def user-db (ref {}))

(defn create-user [id username first_name last_name  gender country]
  "Create a user and add her to the internal database"
  (let
      [user (->User id username first_name last_name  gender country)]
    (dosync
     (when-not (@user-db id)
       (alter user-db assoc id user)))))

(defn update-user [id username first_name last_name  gender country]
  "Update the values for an existing user in the internal database"
  (if (get @user-db id)
    (let
        [user (->User id username first_name last_name  gender country)]
      (dosync
       (alter user-db assoc id user)))))

(defn get-user-by-id [id]
  (get @user-db id))

(defn list-users []
  (vals @user-db))

(defn get-user-by-username [username]
  (filter #(= (:username %) username) (list-users)))

;(defn relate-users [])

(defn list-users-handler []
  {:status 200
   :body (json/generate-string (list-users))})

(defn get-user-by-id-handler [id]
  {:status 200
   :body (json/generate-string (get-user-by-id id))})

(defroutes app
  (GET "/list" request "Hello")
  (GET ["/user/:id" :id #"\d+"] [id] (get-user-by-id-handler id)))






