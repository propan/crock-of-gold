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
              [ring.util.response :as ring-resp]))

(defn read-source-file
  [name]
  (slurp (io/as-file name)))

(defn home-page
  [request]
  (jade/index-page {}))

(defn hiccup-page
  [request]
  (hiccup/hiccup-page {:file (read-source-file "./src/crock_of_gold/views/hiccup.clj")}))

(defn enlive-page
  [request]
  (enlive/enlive-page {:file (read-source-file "./src/crock_of_gold/views/enlive.clj")}))

(defn selmer-page
  [request]
  (selmer/selmer-page {:file (read-source-file "./resources/templates/selmer/selmer-page.html")}))

(defn jade-page
  [request]
  (jade/jade-page {:file (read-source-file "./resources/templates/jade/jade-page.jade")}))

(defroutes routes
  [[["/" {:get home-page}
     ^:interceptors [(body-params/body-params) bootstrap/html-body]
     ["/hiccup" {:get hiccup-page}]
     ["/enlive" {:get enlive-page}]
     ["/selmer" {:get selmer-page}]
     ["/clj-jade" {:get jade-page}]]]])

(def service {:env                      :prod
              ::bootstrap/routes        routes
              ::bootstrap/resource-path "/public"
              ::bootstrap/type          :jetty
              ::bootstrap/port          8080})
