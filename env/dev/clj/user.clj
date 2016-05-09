(ns user
  (:require [mount.core :as mount]
            ndcljhttpd.core))

(defn start []
  (mount/start-without #'ndcljhttpd.core/repl-server))

(defn stop []
  (mount/stop-except #'ndcljhttpd.core/repl-server))

(defn restart []
  (stop)
  (start))


