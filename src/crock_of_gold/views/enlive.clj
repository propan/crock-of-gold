(ns crock-of-gold.views.enlive
  (:require [net.cgrand.enlive-html :as h :refer [deftemplate defsnippet]]
            [io.pedestal.service.log :as log]
            [ring.util.response :as ring-resp]))

(def add-active-class
  (h/add-class "active"))

(defn- menu-highlighter
  [active]
  #(let [attrs (:attrs %)
         name  (:menu-item attrs)]
     (cond-> (assoc % :attrs (dissoc attrs :menu-item))
             (= name active) add-active-class)))

(deftemplate layout "templates/enlive/layout.html"
  [active title content]
  [:head :title]               (h/content title)
  [[:li (h/attr? :menu-item)]] (menu-highlighter "enlive")
  [:#content]                  (h/content content))

(defsnippet navigation-pills-snippet "templates/enlive/navigation-pills.html" [:#content :> :*]
  [active]
  [[:li (h/attr? :menu-item)]] (menu-highlighter active))

(defn transform-field
  [fields]
  (fn [node]
    (let [field-name (keyword (get-in node [:attrs :form-field]))
          value      (get-in fields [:data field-name] "")
          error      (get-in fields [:data-errors field-name])]
      (h/at node
        [h/root]        (h/do->
                         (h/remove-attr :form-field)
                         (if (nil? error)
                           identity
                           (h/add-class "has-error")))
        [:input]        (h/set-attr :value value)
        [:p.help-block] (when-not (nil? error)
                          (h/content error))))))

(defn transform-alert
  [success name]
  (fn [node]
    (when-not (nil? success)
      (h/at node
            [h/any-node] (h/replace-vars {:name name})))))

(defsnippet enlive-signup-snippet "templates/enlive/enlive-signup-content.html" [:#content :> :*]
  [{:keys [title success name] :as context}]
  [:h1]                    (h/content title)
  [:div.example]           (h/substitute (navigation-pills-snippet "form"))
  [:div.alert]             (transform-alert success name)
  [(h/attr? :form-field )] (transform-field context))

(defsnippet enlive-sources-snippet "templates/enlive/enlive-sources-content.html" [:#content :> :*]
  [{:keys [title files] :as context}]
  [:h1]                        (h/content title)
  [:div.example]               (h/substitute (navigation-pills-snippet "sources"))
  [:div.example-content :> :*] (h/clone-for [{:keys [name type content]} files]
                                            [:.file-name] (h/content name)
                                            [:code] (h/do->
                                                    (h/set-attr :class type)
                                                    (h/content content))))

(defn enlive-signup-page
  [context]
  (ring-resp/response
   (apply str (layout "enlive" "Enlive :: Demo"
                      (enlive-signup-snippet (merge context {:title "Enlive"}))))))

(defn enlive-sources-page
  [context]
  (ring-resp/response
   (apply str (layout "enlive" "Enlive :: Demo"
                      (enlive-sources-snippet (merge context {:title "Enlive"}))))))
