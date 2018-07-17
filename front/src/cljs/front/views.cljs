(ns front.views
  (:require
   [re-frame.core :as re-frame]
   [front.subs :as subs]))
   

;; home

(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [:h4
     (str "generic-ui app")]))

(defn link-to-about-page []
  [:a.btn
   {:href "#/about"}
   "go to About Page"])

(defn row [info]
  [:div.test info])

(defn list-of-items []
  (let [items (re-frame/subscribe [::subs/items-currently-selected])]
    (fn []
        [:div
            [:ol
                (for [item @items]
                    [:li [row item]])]])))
                     

(defn home-panel []
  [:div
   [home-title]
   [list-of-items]
   [link-to-about-page]])
    
;; about, unused until later

(defn about-title []
  [:h2
   "This is the About Page."])

(defn link-to-home-page []
  [:a.btn.primary
   {:href "#/"}
   "go to Home Page"])

(defn about-panel []
  [:div
   [about-title]
   [link-to-home-page]])


;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div
     [panels @active-panel]]))
