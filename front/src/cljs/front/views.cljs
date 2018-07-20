(ns front.views
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [front.subs :as subs]))
;; keyboard


;; home

(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [:h4
     (str "generic-ui app")]))

(defn link-to-about-page []
  [:a.btn
   {:on-click #(re-frame/dispatch [:down])} "go to about page"])

(defn link-to-about-page-2 []
  [:a.btn
   {:on-click #(re-frame/dispatch [:up])} "go to other page"])

(defn row [info]
  [:div.test info])

(defn list-of [items selected]
  [:div.c
   [:div.container.card
    (doall
     (for [item @items :let [selected @selected]]
       ^{:key item} [:div.item {:class (if (= item selected) "highlighted")}
                     [row item]]))]])


(defn selected []
  (let [items (re-frame/subscribe [::subs/titles-currently-selected])
        selected (re-frame/subscribe [::subs/title-currently-previewing])]
    [list-of items selected]))


(defn preview []
  (let [items (re-frame/subscribe [::subs/titles-currently-previewing])]
    [list-of items items])) ;; very sloppy and bad

(defn home-panel []
  [:div
   [home-title]
   [selected]
   [preview]])

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
