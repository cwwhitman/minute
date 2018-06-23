(ns front.core
    (:require
      [reagent.core :as r]))

(defonce list-of-events
  (r/atom [["brush teeth" "10 mins"] ["work on project" "2 hours"] ["sleep" "8 hours"]]))
;; -------------------------
;; Views
(defn main-panel [what children]
  [:div {:style {:background "pink" :padding "10px"}}
    children])

(defn line [a b]
  [:div.line a [:span.right b]])

(defn group [children]
  (let [selected nil]
    (fn [children]
      [:div
       (for [[a b] children]
         (line a b))])))

(defn home-page []
  [main-panel
   ;;(js/console.log list-of-events)
   (println @list-of-events)
   [:div [:h4 "Welcome to Generic UI"]
    [group @list-of-events]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
