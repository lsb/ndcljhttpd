(ns ndcljhttpd.routes.home
  (:require [ndcljhttpd.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [ndcljhttpd.db.core :as db]
            [nd.htmlconversions]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn default-session []
  {:id (System/currentTimeMillis)
   :slider 3
   :pparts true
   :notes true
   :language "english"})

(defn get-session-lol [] {})

(defn odi-et-amo [] (time (nd.htmlconversions/subpassageid-to-html db/*db* (:id (first (db/first-subpassage-of-passage-in-book {:bookauthor "Juvenal" :booktitle "Satires" :passagetitle "6"}))))))

(defn front-page []
  (layout/make-a-page {:text (odi-et-amo)} (merge (default-session) (get-session-lol)) "/"))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/homelol" [] (home-page))
  (GET "/" [] (front-page))
  (GET "/about" [] (about-page)))

