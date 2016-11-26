(ns ndcljhttpd.layout
  (:require [selmer.parser :as parser]
            [selmer.filters :as filters]
            [markdown.core :refer [md-to-html-string]]
            [ring.util.http-response :refer [content-type ok]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))


(declare ^:dynamic *app-context*)
(parser/set-resource-path!  (clojure.java.io/resource "templates"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))
(filters/add-filter! :markdown (fn [content] [:safe (md-to-html-string content)]))

(defn render
  "renders the HTML template located relative to resources/templates"
  [template & [params]]
  (content-type
    (ok
      (parser/render-file
        template
        (assoc params
          :page template
          :csrf-token *anti-forgery-token*
          :servlet-context *app-context*)))
    "text/html; charset=utf-8"))

(defn logged-in [session] (< (:id session) 100000000))

(defn user-link [u] (str "/people/" (:urlname u)))

(defn make-a-page
  "render page.html.selmer with sensible defaults"
  [options session request-uri]
  (let [salutation
          (if (not (logged-in session))
             (str "Hi there. <a rel='nofollow' href='" (hiccup.util/url "/login" {:from request-uri}) "'>Login or signup free.</a>")
             (let [username (:name (:user session))
                   upcase (not= (clojure.string/lower-case username) username)]
               (str (if upcase "Hello, " "hi ")
                    " "
                    username
                    ". <a href='"
                    (hiccup.util/url (user-link (:user session)))
                    "'>"
                    (if upcase "V" "v")
                    "iew profile</a> &middot; <a href='"
                    (hiccup.util/url "/logout" {:from request-uri}))))]
    (render "page.html.selmer"
      (merge
        {
          :author "Author"
          :title "Title"
          :javascript ""
          :window_title (str (:author options) " " (:title options) ", with adjustable running vocabulary under each word.")
          :page_description (str "Read " (:author options) " " (:title options) ", with adjustable running vocabulary under each word.")
          :text "Text"
          :no_text_page true
          :slider_value (:slider session)
          :principal_parts (:pparts session)
          :text_notes (:notes session)
          :login_string salutation
          :language (:language session)
          :blue_box true
        }
        options))))

(defn error-page
  "error-details should be a map containing the following keys:
   :status - error status
   :title - error title (optional)
   :message - detailed error message (optional)

   returns a response map with the error page as the body
   and the status specified by the status key"
  [error-details]
  {:status  (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (parser/render-file "error.html" error-details)})
