(ns front.events
  (:require
   [re-frame.core :as re-frame]
   [front.db :as db]
   [front.subs :as subs]
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
            (update db :preview-frame-visual #(max (dec %) 0))))

(re-frame/reg-event-fx
 :up
 [ (re-frame/inject-cofx :length-of-selected)]
 (fn-traced [cofx _]
            (let [db (:db cofx)
                  len (dec (:length-of-selected cofx))]
              {:db (update db :preview-frame-visual #(min (inc %) len))})))

(re-frame/reg-cofx
 :length-of-selected
 (fn [cofx _]
   (assoc cofx :length-of-selected @(re-frame/subscribe [::subs/length-of-selected]))))
