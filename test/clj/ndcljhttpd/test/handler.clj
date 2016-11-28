(ns ndcljhttpd.test.handler
  (:require [clojure.test :refer :all]
            [mount.core]
            [ring.mock.request :refer :all]
            [ndcljhttpd.handler :refer :all]))

(mount.core/start)

(deftest test-app
  (testing "main route"
    (let [response ((app) (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
