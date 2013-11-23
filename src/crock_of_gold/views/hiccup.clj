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

(defn- navigation-pills
  [active]
  [:div {:class "example"}
   [:ul {:class "nav nav-pills nav-justified"}
    [:li (when (= active "form") {:class "active"})
     [:a {:href "/hiccup"} "Signup Form"]]
    [:li (when (= active "sources") {:class "active"})
     [:a {:href "/hiccup/sources"} "Sources"]]]])

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

(defn- form-input
  [context type name label placeholder]
  (let [key   (keyword name)
        error (get-in context [:data-errors key])]
    [:div {:class (if error "form-group has-error" "form-group")}
     [:label {:for name :class "control-label hidden"} label]
     [:input {:type type :class "form-control" :id name :name name :placeholder placeholder :autocomplete "off" :value (get-in context [:data key])}]
     (when error [:p {:class "help-block"} error])]))

(defn- signup-form
  [context]
  [:div {:class "example-content"}
   [:div {:class "expand col-lg-8 col-lg-offset-2"}
    (when-let [success (:success context)]
      [:div {:class "alert alert-success"} (str "You are a great person, " (:username context) "!")])
    [:div {:class "col-lg-10 col-lg-offset-1"}
     [:form {:method "POST" :action "/hiccup" :accept-charset "UTF-8"}
      [:fieldset
       [:legend "Sign up"]
       (form-input context "text" "username" "Username:" "Username")
       (form-input context "text" "email" "Email:" "bob@the-bobs.com")
       (form-input context "password" "password" "Password:" "Password")
       [:div {:class "form-group"}
        [:button {:type "submit" :class "btn btn-default btn-block"} "Sign up"]]]]]]])

(defn hiccup-signup-page
  [context]
  (layout "hiccup" "Hiccup Signup :: Demo"
          [:h1 "Hiccup"]
          (navigation-pills "form")
          (signup-form context)))

(defn- render-file
  [file]
  [:div
   [:div {:class "file-name"}
    (:name file)]
   [:pre [:code {:class "clojure"}
          (:content file)]]])

(defn hiccup-sources-page
  [context]
  (layout "hiccup" "Hiccup :: Demo"
          [:h1 "Hiccup"]
          (navigation-pills "sources")
          [:div {:class "example-content"}
           (map render-file (:files context))]))
