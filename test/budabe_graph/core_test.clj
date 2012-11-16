(ns budabe-graph.core-test
  (:use clojure.test
        budabe-graph.core))

(deftest user-test
  (testing "Test the create-user and get-user-by-username functions"
    (is (= (count @user-db) 0))
    (create-user "johnsmith" "John" "Smith" "male" "US")
    (is (= (count @user-db) 1))
    (create-user "johnsmith" "John" "Smith" "male" "US")
    (is (= (count @user-db) 1))
    (create-user "janegreen" "Jane" "Green" "female" "DE")
    (is (= (count @user-db) 2))    
    (is (= (:last_name (get-user-by-username "janegreen") "Green"))))

  (testing "Test the update-user function"
    (update-user "johnsmith" "John" "Brown" "male" "FR")
    (is (= (:last_name (get-user-by-username "johnsmith") "Brown"))))

  (testing "Test the list-users function"
    (is (= (set (map :last_name (list-users))) (set '("Green" "Brown")))))

)