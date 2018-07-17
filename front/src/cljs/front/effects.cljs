(ns front.effects
  (:require
   [re-frame.core :as re-frame]
   [front.db :as db]))
   

(re-frame/reg-fx
 :butterfly
 (fn [value]))
   
