(ns budabe-graph.core-test
  (:use clojure.test
        budabe-graph.core))

;; (defn reset-db []
;;   (dosync
;;     (alter user-db {})))

;; (defn reset-fixture [f]
;;   (reset-db)
;;   (f))

;;(use-fixtures :each reset-fixture)

(deftest user-test
  (testing "Test the create user functionality"
    (is 
     (= (count @user-db) 0))
    (create-user 1 "johnsmith" "John" "Smith" "male" "US")
    (is 
     (= (count @user-db) 1))
    (is 
     (= (:username (get @user-db 1)) "johnsmith"))
    ;you cannot create the same user twice
    (create-user 1 "johnsmith" "John" "Smith" "male" "US")
    (is 
     (= (count @user-db) 1))
    (create-user 2 "janegreen" "Jane" "Green" "female" "US")
    (is 
     (= (count @user-db) 2)))
  (testing "Test the list user functionality"
    (is
     (= (sort (keys @user-db)) '(1 2)))
    (is 
     (= (set (list-users)) (set (list (->User 1 "johnsmith" "John" "Smith" "male" "US") (->User 2 "janegreen" "Jane" "Green" "female" "US"))))))
  (testing "Test the update user functionality"
    (update-user 1 "johnsmith" "James" "Smith" "male" "FR")
    (is
     (= (:first_name (get @user-db 1)) "James"))
    (is 
     (= (count @user-db) 2))))
    
    
    