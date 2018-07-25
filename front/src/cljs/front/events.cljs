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
 :in
 [(re-frame/inject-cofx :currently-previewing)]
 (fn-traced [cofx _]
            (let [db (:db cofx)
                  id->data (:id->data db)
                  currently-previewing (:currently-previewing cofx)]
              (if (empty? (get-in id->data [currently-previewing :children])) ;; bad, this should not be in view
                {:db db}
                {:db (-> db
                         (assoc :preview-frame-visual 0)
                         (assoc :selected-frame-id currently-previewing)
                         (update :navigation-stack conj (:selected-frame-id db))
                         (update :navigation-stack-titles conj (get-in id->data [currently-previewing :data])))}))))



(re-frame/reg-cofx
 :currently-previewing
 (fn [cofx _] ;; bad because im not supposed to use a sub in
   ;; an event handler if it's not in a view also, according to 
   ;; https://github.com/vimsical/re-frame-utils/blob/master/src/vimsical/re_frame/cofx/inject.cljc
   (assoc cofx :currently-previewing @(re-frame/subscribe [::subs/currently-previewing]))))

(re-frame/reg-event-db
 :out
 (fn-traced [db _]
            (let [stack (:navigation-stack db)
                  title-stack (:navigation-stack-titles db)]
              (if (empty? stack)
                db
                (-> db
                    (assoc :navigation-stack-titles (pop title-stack))
                    (assoc :navigation-stack (pop stack))
                    (assoc :selected-frame-id (last stack)))))))

(re-frame/reg-event-db
 :go ;; TODO needs to update the navigation stack
 (fn-traced [db [_ id]]
            (let [curstack (:navigation-stack db)
                  newstack (loop [stack curstack n id]
                             (if (= (peek stack) n)
                               (pop stack)
                               (recur (pop stack) n)))]
              (-> db
                  (assoc :selected-frame-id id)
                  (assoc :navigation-stack-titles
                         (subvec (:navigation-stack-titles db) 0 (count newstack)))
                  (assoc :navigation-stack newstack)))))


(defn make-new-item [data]
  {:t :node :data data :state "view" :children []})

(re-frame/reg-event-db
 :add-neighbor
 (fn-traced [db _]
            (let [id (:max-id db)
                  current (:selected-frame-id db)]
              (-> db
                  (assoc :max-id (inc id))
                  (update :id->data assoc id (make-new-item "child"))
                  (update-in [:id->data current :children] conj id)))))       

(re-frame/reg-event-fx ;;TODO make this focus/edit the new child
 :add-child
 [(re-frame/inject-cofx :currently-previewing)]
 (fn-traced [{:keys [db currently-previewing]} _]
            (let [id (:max-id db)]
              {:db (-> db
                       (assoc :max-id (inc id))
                       (update :id->data assoc id (make-new-item "child"))
                       (update-in [:id->data currently-previewing :children] conj id))})))       

(re-frame/reg-event-fx
 :edit
 [(re-frame/inject-cofx :currently-previewing)]
 (fn-traced [{:keys [db currently-previewing]} _]
            {:db
             (-> db
                 (assoc-in [:id->data currently-previewing :state] "edit"))}))

(re-frame/reg-event-fx
 :edit-save
 [(re-frame/inject-cofx :currently-previewing)]
 (fn-traced [{:keys [db currently-previewing]} [_ id new-data]]
            {:db
             (-> db
                 (assoc-in [:id->data currently-previewing :state] "view")
                 (assoc-in [:id->data currently-previewing :data] new-data))}))
