(ns budabe-graph.core)

(defrecord User [username, first_name, last_name, gender, locale])

(defrecord Connection [id, type, from_object, to_object])

(def user-db (ref {}))

(defn create-user [username, first_name, last_name, gender, locale]
  "Create a user and add to the internal database"
  (let
      [user (->User username, first_name, last_name, gender, locale)]
    (dosync 
     (when-not (@user-db username)
       (alter user-db assoc username user)))))

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
   