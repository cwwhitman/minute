(ns front.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [secretary.core :as secretary]
   [goog.events :as gevents]
   [goog.history.EventType :as EventType]
   [re-frame.core :as re-frame]
   [front.events :as events]
   [re-pressed.core :as rp]
   ))

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (re-frame/dispatch [::events/set-active-panel :home-panel])
    )

  (defroute "/about" []
    (re-frame/dispatch [::events/set-active-panel :about-panel])
    )

  (re-frame/dispatch
   [::rp/set-keydown-rules
    {:event-keys [
                  [
                   [:in]
                   [{:which 13}]]
                  [
                   [:down]
                   [{:which 38}]]
                  [
                   [:up]
                   [{:which 40}]]]
     :clear-keys [[{:which 27}]]
     :always-listen-keys [{:which 97}]
     :prevent-default-keys []}])


  ;; --------------------
  (hook-browser-navigation!))
