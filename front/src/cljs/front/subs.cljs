(ns front.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub ;; for other subs
 :preview-frame-visual
 (fn [db]
   (:preview-frame-visual db)))

(re-frame/reg-sub ;; for view
 ::preview-frame-visual
 (fn [db]
   (:preview-frame-visual db)))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :currently-selected
 (fn [db]
   (:selected-frame-id db)))

(re-frame/reg-sub
 ::currently-previewing
 :<- [::items-currently-selected]
 :<- [::preview-frame-visual]
 (fn [[items-currently-selected preview-frame-visual] _]
   (get (:children items-currently-selected) preview-frame-visual)))

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
 ::titles-currently-previewing
 :<- [::currently-previewing]
 :<- [:id->data]
 (fn [[currently-previewing id->data] _]
   (for [id (:children (get id->data currently-previewing))]
     (:data (get id->data id))))) ;; I wish this did :data in a different sub, but oh well

(re-frame/reg-sub
 ::length-of-selected
 :<- [::titles-currently-selected]
 (fn [titles _]
   (count titles)))

(re-frame/reg-sub
 ::titles-currently-selected
 :<- [::items-currently-selected]
 :<- [:id->data]
 (fn [[currently-selected id->data] _]
   (for [child (:children currently-selected)]
     (:data (get id->data child)))))

;;(re-frame/reg-sub)
 ;;::titles-currently-previewing
 ;;:<- [::items-currently-previewing]
 ;;(fn [items-currently-previewing _])
   ;;(for [item items-currently-previewing])
     ;;(:data item))))


(re-frame/reg-sub
 ::title-currently-previewing
 :<- [:currently-previewing]
 :<- [:id->data]
 (fn [[currently-previewing id->data] _]
   (:data (get id->data currently-previewing))))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::navigation-stack-titles
 (fn [db]
   (:navigation-stack-titles db)))

(re-frame/reg-sub
 ::navigation-stack-ids
 (fn [db]
   (:navigation-stack db)))
