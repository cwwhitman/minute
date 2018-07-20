(ns front.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [front.events :as events]
   [front.routes :as routes]
   [front.views :as views]
   [front.config :as config]
   [re-pressed.core :as rp]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (routes/app-routes)

  (dev-setup)
  (mount-root))
