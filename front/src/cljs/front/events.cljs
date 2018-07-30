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
 (fn-traced [cofx [_ idx]]
            (let [db (:db cofx)
                  id->data (:id->data db)
                  currently-previewing (:currently-previewing cofx)]
              (console.log idx)
              (if (empty? (get-in id->data [currently-previewing :children])) ;; bad, this should not be in view
                {:db db}
                {:db (-> db
                         (assoc :preview-frame-visual (if (integer? idx) idx 0))
                         (assoc :selected-frame-id currently-previewing)
                         (update :navigation-stack conj (:selected-frame-id db))
                         (update :navigation-stack-titles conj (get-in id->data [currently-previewing :data])))}))))

;;TODO make preview(global-idx) function

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
 :go
 (fn-traced [db [_ id]]
            (let [curstack (:navigation-stack db)
                  newstack (loop [stack curstack n id] ;; TODO for learning, reimp with drop-while
                             (if (= (peek stack) n)
                               (pop stack)
                               (recur (pop stack) n)))]
              (-> db
                  (assoc :selected-frame-id id)
                  (assoc :navigation-stack-titles
                         (subvec (:navigation-stack-titles db) 0 (count newstack)))
                  (assoc :navigation-stack newstack)))))

(defn insert [vec pos item] 
    (apply conj (subvec vec 0 pos) item (subvec vec pos))) ;; from mark j. reed on google groups

(defn make-new-item [data]
  {:t :node :data data :state "edit" :children []})

(re-frame/reg-event-db
 :add-neighbor
 (fn-traced [db _]
            (let [id (:max-id db)
                  current (:selected-frame-id db)
                  current-preview-index (:preview-frame-visual db)]
              (-> db
                  (assoc :max-id (inc id))
                  (update :id->data assoc id (make-new-item ""))
                  (update-in [:id->data current :children] insert (inc current-preview-index) id)       
                  (update :preview-frame-visual inc)
                  (assoc :max-id (inc id))))))


(re-frame/reg-event-fx ;;TODO make this focus/edit the new child
 :add-child
 [(re-frame/inject-cofx :currently-previewing)]
 (fn-traced [{:keys [db currently-previewing]} _] ;; doesn't work with fn-traced because of ->
   (let [id (:max-id db)
         len (-> db :id->data (get currently-previewing) :children (count))]
     (re-frame/dispatch [:in len]) ;; NEXT have preview frame visual as arg
     {:db (-> db ;; NEXT make this call in, instead of messing with stuff
              (update :max-id inc)
              ;; (assoc :selected-frame-id currently-previewing)
              (update :id->data assoc id (make-new-item ""))
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
