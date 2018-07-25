(ns front.db)

(def default-db
  {:name "re-frame"
   :selected-frame-id 0
   :preview-frame-visual 0
   :navigation-stack []
   :navigation-stack-titles []
   :max-id 9
   :id->data {0 {:data "root" :state "view" :children [1 2 3 4]}
              1 {:data "first task" :state "view"  :children [2]}
              2 {:data "second task" :state "view"  :children [3 4]}
              3 {:data "third task" :state "view"  :children []}
              4 {:data "fourth task" :state "view"  :children [1]}
              5 {:data "fifth task" :state "view"  :children []}
              6 {:data "sixth task" :state "view"  :children []}
              7 {:data "seventh task" :state "view"  :children []}
              8 {:data "eighth task" :state "view" :children []}
              9 {:data "ninth task" :state "edit" :children []}}})

;; https://github.com/yatesco/re-frame-stitching-together/blob/master/src/cljs/demo/views.cljs#L81
