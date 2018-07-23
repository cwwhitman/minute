(ns front.events
  (:require
   [re-frame.core :as re-frame]
   [front.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))
   

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [cofx [_ active-panel]]
            {:db (assoc (:db cofx) :active-panel active-panel)}))

(re-frame/reg-event-db
 :down
 (fn-traced [db _]
            (update db :preview-frame-visual dec)))

(re-frame/reg-event-db
 :up
 (fn-traced [db _]
            (update db :preview-frame-visual inc)))
