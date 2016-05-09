(ns ndcljhttpd.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [ndcljhttpd.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[ndcljhttpd started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[ndcljhttpd has shutdown successfully]=-"))
   :middleware wrap-dev})
