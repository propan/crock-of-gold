(ns crock-of-gold.views.hiccup
  (:require [hiccup.def :refer [defelem]]
            [hiccup.form :refer [submit-button]]
            [hiccup.page :refer [html5 include-js include-css]]
            [hiccup.element :refer [link-to]]
            [ring.util.response :as ring-resp]))

(defn head
  [title]
  [:head
   [:title title]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   (include-css "/css/bootstrap.min.css" "/css/custom.css" "/css/railscasts.css")
   "<!--[if lt IE 9]>"
   (include-js "/js/html5shiv.js" "/js/respond.min.js")
   "<![endif]-->"])

(defelem navigation
  [active xs]
  [:ul
   (for [x xs]
     [:li (when (= active (first x)) {:class "active"})
      (apply link-to (rest x))])])

(defn navbar
  [active]
  [:header
   [:nav {:class "navbar navbar-default" :role "navigation"}
    [:div {:class "navbar-header"}
     [:button {:type "button" :class "navbar-toggle" :data-toggle "collapse" :data-target ".navbar-ex1-collapse"}
      [:span {:class "sr-only"} "Toggle navigation"]
      (repeat 3 [:span {:class "icon-bar"}])]
     (link-to {:class "navbar-brand"} "/" "The Crock of Gold")]
    [:div {:class "collapse navbar-collapse navbar-ex1-collapse"}
     (navigation {:class "nav navbar-nav"}
                 active [["home" "/" "Home"]
                         ["hiccup" "/hiccup" "Hiccup"]
                         ["enlive" "/enlive" "Enlive"]
                         ["selmer" "/selmer" "Selmer"]
                         ["clj-jade" "/clj-jade" "clj-jade"]])]]])

(defn- navigation-pills
  [active]
  [:div {:class "example"}
   (navigation {:class "nav nav-pills nav-justified"}
               active [["form" "/hiccup" "Signup Form"]
                       ["sources" "/hiccup/sources" "Sources"]])])

(defn layout
  [active title & content]
  (ring-resp/response
   (html5
    (head title)
    [:body
     (navbar active)
     [:section {:id "content" :class "container"}
      content]
     [:footer
      [:nav {:class "navbar navbar-default navbar-fixed-bottom" :role "navigation"}
       [:p {:class "navbar-text pull-right"} "The Crock of Gold &copy; 2013"]]]
     [:a {:href "https://github.com/propan/crock-of-gold"}
      [:img {:style "position: absolute; top: 0; right: 0; border: 0; z-index: 6000;"
             :src "https://s3.amazonaws.com/github/ribbons/forkme_right_orange_ff7600.png"
             :alt "Fork me on GitHub"}]]
     (include-js "//code.jquery.com/jquery.js" "/js/bootstrap.min.js" "/js/highlight.pack.js")
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
      [:div {:class "alert alert-success"} (str "You are a great person, " (:name context) "!")])
    [:div {:class "col-lg-10 col-lg-offset-1"}
     [:form {:method "POST" :action "/hiccup" :accept-charset "UTF-8"}
      [:fieldset
       [:legend "Sign up"]
       (form-input context "text" "username" "Username:" "Username")
       (form-input context "text" "email" "Email:" "bob@the-bobs.com")
       (form-input context "password" "password" "Password:" "Password")
       [:div {:class "form-group"}
        (submit-button {:class "btn btn-default btn-block"} "Sign up")]]]]]])

(defn hiccup-signup-page
  [context]
  (layout "hiccup" "Hiccup :: Demo"
          [:h1 "Hiccup"]
          (navigation-pills "form")
          (signup-form context)))

(defn- render-file
  [file]
  [:div
   [:div {:class "file-name"}
    (:name file)]
   [:pre [:code {:class (:type file)}
          (:content file)]]])

(defn hiccup-sources-page
  [context]
  (layout "hiccup" "Hiccup :: Demo"
          [:h1 "Hiccup"]
          (navigation-pills "sources")
          [:div {:class "example-content"}
           (map render-file (:files context))]))
