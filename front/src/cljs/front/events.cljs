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
 [(re-frame/inject-cofx :length-of-selected)]
 (fn-traced [cofx _]
            (let [db (:db cofx)
                  len (dec (:length-of-selected cofx))]
              {:db (update db :preview-frame-visual #(min (inc %) len))})))

(re-frame/reg-cofx
 :length-of-selected
 (fn [cofx _]
   (assoc cofx :length-of-selected @(re-frame/subscribe [::subs/length-of-selected]))))

(re-frame/reg-event-fx
 :on
 [(re-frame/inject-cofx :currently-previewing)]
 (fn-traced [cofx _]
            (let [db (:db cofx)
                  id->data (:id->data db)
                  currently-previewing (:currently-previewing cofx)]
              (if (empty? (get-in id->data [currently-previewing :children])) ;; bad, this should not be in view
                {:db db}
                {:db (-> db
                         (assoc :selected-frame-id currently-previewing)
                         (update :navigation-stack conj (:selected-frame-id db)))}))))
                     
                          

(re-frame/reg-cofx
 :currently-previewing
 (fn [cofx _] ;; bad because im not supposed to use a sub in
   ;; an event handler if it's not in a view also, according to 
   ;; https://github.com/vimsical/re-frame-utils/blob/master/src/vimsical/re_frame/cofx/inject.cljc
   (assoc cofx :currently-previewing @(re-frame/subscribe [::subs/currently-previewing]))))

(re-frame/reg-event-db
 :out
 (fn-traced [db _]
            (let [stack (:navigation-stack db)]
              (if (empty? stack)
                db
                (-> db
                    (assoc :navigation-stack (pop stack))
                    (assoc :selected-frame-id (last stack)))))))
