(ns front.db)


(defn generate-task [s n]
  (for [x (range n)]
    {:id (+ s x) :title (str "example task number " n) :children []}))
    

(def default-db
  {:name "re-frame"
   :selected-frame-id 0
   :nested [{:id 0 :title "root"
             :children [{:id 1 :title "first task" :children []}
                        {:id 2 :title "second task" :children []}
                        {:id 3 :title "third task" :children (generate-task 4 10)}]}]})

;; https://github.com/yatesco/re-frame-stitching-together/blob/master/src/cljs/demo/views.cljs#L81
