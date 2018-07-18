(ns front.db)

(def default-db
  {:name "re-frame"
   :selected-frame-id 1
   :preview-frame-id 3
   :id->data {0 {:data "root" :children [1 2 3 4]}
              1 {:data "first task" :children [3]}
              2 {:data "second task" :children []}
              3 {:data "third task" :children []}
              4 {:data "fourth task" :children []}}})

;; https://github.com/yatesco/re-frame-stitching-together/blob/master/src/cljs/demo/views.cljs#L81
