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
  [:div.c.6.col
   [:div.container.card
    (doall
     (for [item @items :let [selected @selected]]
       ^{:key item} [:div.item {:class (if (= item selected) "highlighted")}
                     [row item]]))]])

(defn list-of-2 [items selected]
  [:div.c.6.col
   [:div.container.card
    (let [func (fn [[i item]]
                ^{:key item} [:div.item {:class (if (= i @selected) "highlighted")} [row item]])]
        (doall (map (comp func vector) (range) @items)))]])


(defn selected []
  (let [items (re-frame/subscribe [::subs/titles-currently-selected])
        selected (re-frame/subscribe [::subs/preview-frame-visual])]
    [list-of-2 items selected]))

(defn empty-list []
  [:div.c.6.col
   [:div
    [:div]]])
        

(defn preview []
  (let [items (re-frame/subscribe [::subs/titles-currently-previewing])]
    (if (> (count @items) 0)
      [list-of items items] ;; very sloppy and bad
      [empty-list])))

     

(defn home-panel []
  [:div
    [home-title]
   [:div.row
    [selected]
    [preview]]])

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
