(ns crock-of-gold.views.hiccup
  (:require [hiccup.page :as h]
            [hiccup.element :as e]
            [ring.util.response :as ring-resp]))

(defn head
  [title]
  [:head
   [:title title]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   [:link {:href "/css/bootstrap.min.css" :rel "stylesheet" :type "text/css"}]
   [:link {:href "/css/custom.css" :rel "stylesheet" :type "text/css"}]
   [:link {:href "/css/railscasts.css"  :rel "stylesheet" :type "text/css"}]   
   "<!--[if lt IE 9]>"
   [:script {:src "/js/html5shiv.js"}]
   [:script {:src "/js/respond.min.js"}]
   "<![endif]-->"])

(defn navbar
  [active]
  [:header
   [:nav {:class "navbar navbar-default" :role "navigation"}
    [:div {:class "navbar-header"}
     [:button {:type "button" :class "navbar-toggle" :data-toggle "collapse" :data-target ".navbar-ex1-collapse"}
      [:span {:class "sr-only"} "Toggle navigation"]
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]]
     [:a {:class "navbar-brand" :href "/"} "The Crock of Gold"]]
    [:div {:class "collapse navbar-collapse navbar-ex1-collapse"}
     [:ul {:class "nav navbar-nav"}
      [:li (when (= active "home") {:class "active"})
       [:a {:href "/"} "Home"]]
      [:li (when (= active "hiccup") {:class "active"})
       [:a {:href "/hiccup"} "Hiccup"]]
      [:li (when (= active "enlive") {:class "active"})
       [:a {:href "/enlive"} "Enlive"]]
      [:li (when (= active "selmer") {:class "active"})
       [:a {:href "/selmer"} "Selmer"]]
      [:li (when (= active "clj-jade") {:class "active"})
       [:a {:href "/clj-jade"} "clj-jade"]]]]]])

(defn layout
  [active title & content]
  (ring-resp/response
   (h/html5
    (head title)
    [:body
     (navbar active)
     [:section {:id "content" :class "container"}
      content]
     [:footer
      [:nav {:class "navbar navbar-default navbar-fixed-bottom" :role "navigation"}
       [:p {:class "navbar-text pull-right"}
        "The Crock of Gold &copy; 2013"]]]
     [:script {:src "//code.jquery.com/jquery.js"}]
     [:script {:src "/js/bootstrap.min.js"}]
     [:script {:src "/js/highlight.pack.js"}]
     [:script
      "hljs.initHighlightingOnLoad();"]])))

(defn hiccup-page
  [context]
  (layout "hiccup" "Hiccup :: Demo"
          [:h1 "Hiccup"]
          [:pre
           [:code {:class "clojure"}
            (:file context)]]))
