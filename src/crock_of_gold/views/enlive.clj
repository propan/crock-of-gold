(ns crock-of-gold.views.enlive
  (:require [net.cgrand.enlive-html :as html]
            [io.pedestal.service.log :as log]
            [ring.util.response :as ring-resp]))

(def add-active-class
  (html/add-class "active"))

(defn- menu-highlighter
  [active]
  #(let [attrs (:attrs %)
         name  (:menu-item attrs)]
     (cond-> (assoc % :attrs (dissoc attrs :menu-item))
             (= name active) add-active-class)))

(html/deftemplate layout "templates/enlive/layout.html"
  [active title content]
  [:head :title]                  (html/content title)
  [[:li (html/attr? :menu-item)]] (menu-highlighter "enlive")
  [:#content]                     (html/content content))

(html/defsnippet enlive-page-snippet "templates/enlive/enlive-page-content.html" [:#content :> :*]
  [{:keys [title file] :as context}]
  [:h1]   (html/content title)
  [:code] (html/content file))


(defn enlive-page
  [context]
  (ring-resp/response
   (apply str (layout "enlive" "Enlive :: Demo"
                      (enlive-page-snippet (merge context {:title "Enlive"}))))))
