(defproject crock-of-gold "0.0.1-SNAPSHOT"
  :description "an application to demonstrate usage of some Clojure template languages"
  :url "http://github.com/propan/crock-of-gold"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.pedestal/pedestal.service "0.2.2"]
                 [io.pedestal/pedestal.service-tools "0.2.2"]
                 [io.pedestal/pedestal.jetty "0.2.2"]
                 [hiccup "1.0.4"]
                 [enlive "1.1.4"]
                 [selmer "0.5.3"]
                 [clj-jade "0.1.4"]
                 [formar "0.1.2"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :aliases {"run-dev" ["trampoline" "run" "-m" "crock-of-gold.server/run-dev"]}
  :repl-options  {:init-ns user
                  :init (try
                          (use 'io.pedestal.service-tools.dev)
                          (require 'crock-of-gold.service)
                          ;; Nasty trick to get around being unable to reference non-clojure.core symbols in :init
                          (eval '(init crock-of-gold.service/service #'crock-of-gold.service/routes))
                          (catch Throwable t
                            (println "ERROR: There was a problem loading io.pedestal.service-tools.dev")
                            (clojure.stacktrace/print-stack-trace t)
                            (println)))
                  :welcome (println "Welcome to pedestal-service! Run (tools-help) to see a list of useful functions.")}
  :main ^{:skip-aot true} crock-of-gold.server)
