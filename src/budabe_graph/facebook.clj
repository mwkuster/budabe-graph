(ns budabe-graph.facebook
  (:use [compojure.core :only (GET PUT POST defroutes)]
        [hiccup.middleware :only (wrap-base-url)])
  (:import (java.net URI))
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [cheshire.core :as json]
            [clj-http.client :as client]
            [ring.util.codec :as codec]
            [clojure.java.browse :as browse]
            [compojure.response :as response]))

(def  ^:dynamic *facebook-credentials* (load-file "resources/configuration.clj"))

(def ^:dynamic *access-token* "")

(defn facebook-authorization-handler []
  "Ask Facebook for the authorization of this application"
   (let
       [auth-uri (str "https://www.facebook.com/dialog/oauth?client_id=" (:client-id *facebook-credentials*) "&redirect_uri=" (codec/url-encode (:redirect-uri *facebook-credentials*)))]
     (println auth-uri)
     ;and open this URI in a browser for further treatment
     (browse/browse-url auth-uri)))

(defn facebook-token-handler [request]
  "Get the actual access-token and install it in the application"
  (println "facebook-handler")
  (println (str "Code:!" (:code (:params request)) "!"))
  (let
      [code (:code (:params request))
       access-token-return
       (client/get "https://graph.facebook.com/oauth/access_token" 
                                {:query-params {"client_id" (:client-id *facebook-credentials*),
                                                "client_secret" (:client-secret* *facebook-credentials*),
                                                "redirect_uri" (:redirect-uri *facebook-credentials*)
                                                "code" code}})
       access-token (second (re-find #".*=([a-zA-Z0-9]*)&.*"(:body access-token-return)))]
    (println (str "access-token:!"  access-token "!"))
    (def ^:dynamic *access-token* access-token)
    {:status 200 :access-token access-token}))

(defn facebook-handler 
  "Get information about a given facebook-id (per default the user which is logged in)"
  ([facebook-id]
     (assert (not (empty? *access-token*)))
     (let
         [me (client/get (str "https://graph.facebook.com/" facebook-id) {:oauth-token *access-token*})]
       (println me)
       me))
  ([]
     (facebook-handler "me")))
