(ns crock-of-gold.service
    (:require [io.pedestal.service.http :as bootstrap]
              [io.pedestal.service.http.route :as route]
              [io.pedestal.service.http.body-params :as body-params]
              [io.pedestal.service.http.route.definition :refer [defroutes]]
              [crock-of-gold.views.enlive :as enlive]
              [crock-of-gold.views.hiccup :as hiccup]
              [crock-of-gold.views.jade :as jade]
              [crock-of-gold.views.selmer :as selmer]
              [clojure.java.io :as io]
              [formar.core :as f :refer [defform email length pattern required]]
              [ring.util.response :as ring-resp]))

(defn read-source-file
  [name]
  (slurp (io/as-file name)))

(defform registration-form
  [[[:username required (pattern #"^[a-zA-Z][a-zA-Z0-9_]+$") (length :min 3 :max 25)]
    [:email required email (length :max 150)]
    [:password required (length :min 6)]]])

(defn home-page
  [request]
  (jade/index-page {}))

(defn signup-handler
  [request handler]
  (let [form (registration-form (:form-params request))]
    (if (f/valid? form)
      (handler {:success true
                :name    (get-in form [:data :username])})
      (handler form))))

(defn hiccup-signup-get
  [request]
  (hiccup/hiccup-signup-page {}))

(defn hiccup-signup-post
  [request]
  (signup-handler request hiccup/hiccup-signup-page))

(defn hiccup-sources-get
  [request]
  (hiccup/hiccup-sources-page {:files [{:name    "src/crock_of_gold/views/hiccup.clj"
                                        :type    "clojure"
                                        :content (read-source-file "./src/crock_of_gold/views/hiccup.clj")}]}))

(defn enlive-signup-get
  [request]
  (enlive/enlive-signup-page {}))

(defn enlive-signup-post
  [request]
  (signup-handler request enlive/enlive-signup-page))

(defn enlive-sources-get
  [request]
  (enlive/enlive-sources-page {:files [{:name    "src/crock_of_gold/views/enlive.clj"
                                        :type    "clojure"
                                        :content (read-source-file "./src/crock_of_gold/views/enlive.clj")}
                                       {:name    "resources/templates/enlive/layout.html"
                                        :type    "html"
                                        :content (read-source-file "./resources/templates/enlive/layout.html")}
                                       {:name    "resources/templates/enlive/enlive-signup-content.html"
                                        :type    "html"
                                        :content (read-source-file "./resources/templates/enlive/enlive-signup-content.html")}
                                       {:name    "resources/templates/enlive/enlive-sources-content.html"
                                        :type    "html"
                                        :content (read-source-file "./resources/templates/enlive/enlive-sources-content.html")}
                                       {:name    "resources/templates/enlive/navigation-pills.html"
                                        :type    "html"
                                        :content (read-source-file "./resources/templates/enlive/navigation-pills.html")}]}))

(defn selmer-signup-get
  [request]
  (selmer/selmer-signup-page {}))

(defn selmer-signup-post
  [request]
  (signup-handler request selmer/selmer-signup-page))

(defn selmer-sources-get
  [request]
  (selmer/selmer-sources-page {:files [{:name    "src/crock_of_gold/views/selmer.clj"
                                        :type    "clojure"
                                        :content (read-source-file "./src/crock_of_gold/views/selmer.clj")}
                                       {:name    "resources/templates/selmer/layout.html"
                                        :type    "django"
                                        :content (read-source-file "./resources/templates/selmer/layout.html")}
                                       {:name    "resources/templates/selmer/selmer-signup-page.html"
                                        :type    "django"
                                        :content (read-source-file "./resources/templates/selmer/selmer-signup-page.html")}
                                       {:name    "resources/templates/selmer/selmer-sources-page.html"
                                        :type    "django"
                                        :content (read-source-file "./resources/templates/selmer/selmer-sources-page.html")}
                                       {:name    "resources/templates/selmer/navigation-pills.html"
                                        :type    "django"
                                        :content (read-source-file "./resources/templates/selmer/navigation-pills.html")}
                                       {:name    "resources/templates/selmer/navbar.html"
                                        :type    "django"
                                        :content (read-source-file "./resources/templates/selmer/navbar.html")}]}))

(defn jade-signup-get
  [request]
  (jade/jade-signup-page {}))

(defn jade-signup-post
  [request]
  (signup-handler request jade/jade-signup-page))

(defn jade-sources-get
  [request]
  (jade/jade-sources-page {:files [{:name    "src/crock_of_gold/views/jade.clj"
                                    :type    "clojure"
                                    :content (read-source-file "./src/crock_of_gold/views/jade.clj")}
                                   {:name    "resources/templates/jade/layout.jade"
                                    :type    "javascript"
                                    :content (read-source-file "./resources/templates/jade/layout.jade")}
                                   {:name    "resources/templates/jade/mixins.jade"
                                    :type    "javascript"
                                    :content (read-source-file "./resources/templates/jade/mixins.jade")}
                                   {:name    "resources/templates/jade/jade-signup-page.jade"
                                    :type    "javascript"
                                    :content (read-source-file "./resources/templates/jade/jade-signup-page.jade")}
                                   {:name    "resources/templates/jade/jade-sources-page.jade"
                                    :type    "javascript"
                                    :content (read-source-file "./resources/templates/jade/jade-sources-page.jade")}]}))

(defroutes routes
  [[["/" {:get home-page}
     ^:interceptors [(body-params/body-params) bootstrap/html-body]
     ["/hiccup" {:get hiccup-signup-get :post hiccup-signup-post}
      ["/sources" {:get hiccup-sources-get}]]
     ["/enlive" {:get enlive-signup-get :post enlive-signup-post}
      ["/sources" {:get enlive-sources-get}]]
     ["/selmer" {:get selmer-signup-get :post selmer-signup-post}
      ["/sources" {:get selmer-sources-get}]]
     ["/clj-jade" {:get jade-signup-get :post jade-signup-post}
      ["/sources" {:get jade-sources-get}]]]]])

(def service {:env                      :prod
              ::bootstrap/routes        routes
              ::bootstrap/resource-path "/public"
              ::bootstrap/type          :jetty
              ::bootstrap/port          (Integer/valueOf (or (System/getenv "PORT") "8080"))})
