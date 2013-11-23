(ns crock-of-gold.views.jade
  (:require [ring.util.response :as ring-resp]
            [clj-jade.core :as jade]))

(jade/configure {:template-dir "resources/templates/jade/"
                 :pretty-print false
                 :cache? true})

(defmacro defview
  [name template]
  `(defn ~name
     [context#]
     (ring-resp/response (jade/render ~template context#))))

(defview index-page "index-page.jade")
(defview jade-signup-page "jade-signup-page.jade")
(defview jade-sources-page "jade-sources-page.jade")
