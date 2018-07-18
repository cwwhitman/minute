(ns front.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :currently-selected
 (fn [db]
   (:selected-frame-id db)))


(re-frame/reg-sub
 :id->data
 (fn [db]
   (:id->data db)))

(re-frame/reg-sub
 ::items-currently-selected
 :<- [:currently-selected]
 :<- [:id->data]
 (fn [[currently-selected id->data] _]
   (get id->data currently-selected)))


(re-frame/reg-sub
 ::names-currently-selected
 :<- [::items-currently-selected]
 :<- [:id->data]
 (fn [[currently-selected id->data] _]
   (for [child (:children currently-selected)]
     (:data (get id->data child)))))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
