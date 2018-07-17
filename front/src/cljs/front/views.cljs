(ns front.views
  (:require
   [re-frame.core :as re-frame]
   [front.subs :as subs]))
   

;; home

(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [:h2
     (str "Hello from " @name ". This is the Home Page.")]))

(defn link-to-about-page []
  [:a.btn
   {:href "#/about"}
   "go to About Page"])

(defn home-panel []
  [:div
   [home-title]
   [link-to-about-page]])
    


;; about

(defn about-title []
  [:h2
   "This is the About Page."])

(defn link-to-home-page []
  [:a.btn
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
