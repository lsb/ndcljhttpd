(ns ndcljhttpd.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[ndcljhttpd started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[ndcljhttpd has shutdown successfully]=-"))
   :middleware identity})
