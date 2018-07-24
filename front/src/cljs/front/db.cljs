(ns front.db)

(def default-db
  {:name "re-frame"
   :selected-frame-id 0
   :preview-frame-visual 0
   :navigation-stack []
   :navigation-stack-titles []
   :id->data {0 {:data "root" :children [1 2 3 4]}
              1 {:data "first task" :children [2]}
              2 {:data "second task" :children [3 4]}
              3 {:data "third task" :children []}
              4 {:data "fourth task" :children [1]}
              5 {:data "fifth task" :children []}
              6 {:data "sixth task" :children []}
              7 {:data "seventh task" :children []}
              8 {:data "eighth task" :children []}
              9 {:data "ninth task" :children []}}})

;; https://github.com/yatesco/re-frame-stitching-together/blob/master/src/cljs/demo/views.cljs#L81
