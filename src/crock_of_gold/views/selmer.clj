(ns crock-of-gold.views.selmer
  (:require [clojure.java.io :as io]
            [ring.util.response :as ring-resp]
            [selmer.parser :refer [render-file]]))

(selmer.parser/set-resource-path! (.getAbsolutePath (io/as-file "./resources/templates/selmer")))

(defn selmer-signup-page
  [context]
  (ring-resp/response
   (render-file "selmer-signup-page.html" (merge context {:active "selmer" :active-pill "form"}))))

(defn selmer-sources-page
  [context]
  (ring-resp/response
   (render-file "selmer-sources-page.html" (merge context {:active "selmer" :active-pill "sources"}))))
