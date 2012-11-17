(ns budabe-graph.core
  (:use [compojure.core :only (GET PUT POST defroutes)]
        [hiccup.middleware :only (wrap-base-url)]
        [budabe-graph.facebook])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [cheshire.core :as json]
            [clj-http.client :as client]
            [compojure.response :as response]))


(defrecord User [username, first_name, last_name, gender, locale])

(defrecord Connection [id, type, from_object, to_object])

(def user-db (ref {}))

(defn create-user [username, first_name, last_name, gender, locale]
  "Create a user and add to the internal database"
  (let
      [user (->User username, first_name, last_name, gender, locale)]
    (dosync 
     (when-not (@user-db username)
       (alter user-db assoc username user)
       username))))

(defn get-user-by-username [username]
  "Get back a user record by its username"
  (get @user-db username))

(defn update-user [username first_name, last_name, gender, locale]
  "Update the values for an existing user in the internal database"
  (if (get @user-db username)
    (let
        [user (->User username, first_name, last_name, gender, locale)]
      (dosync
       (alter user-db assoc username user)))))
       
(defn list-users []
  "Return the information for all existing users in the internal database"
  (vals @user-db))
   
;; Handler
(defn create-user-handler [body] 
  (let
      [input (json/parse-string (slurp body))
       username (create-user (get input "username") (get input "first_name") (get input "last_name") (get input "gender") (get input "locale"))]
    (println input)
    (if (not (empty? username))
      {:status 201 :body username}
      {:status 409}))) ;409: conflict

(defn update-user-handler [body] 
  (let
      [input (json/parse-string (slurp body))
       user (update-user (get input "username") (get input "first_name") (get input "last_name") (get input "gender") (get input "locale"))]
    (println input)
    (if (not (empty? user))
      {:status 204}
      {:status 404})))


;; Routing
(defroutes main-routes
  (GET "/" [] (json/generate-string (list-users)))
  (POST "/"  request (create-user-handler  (:body request)))
  (PUT "/:username" request (update-user-handler  (:body request)))
  (GET "/api/facebook-authorize" [] (facebook-authorization-handler))
  (GET "/api/facebook-callback" request (facebook-token-handler request))
  (GET "/:username" [username] (get-user-by-username username))
  (GET "/facebook/me" [] (facebook-handler))
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)))
