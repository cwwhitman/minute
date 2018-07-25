(ns front.db)

(def default-db
  {:name "re-frame"
   :selected-frame-id 0
   :preview-frame-visual 0
   :navigation-stack []
   :navigation-stack-titles []
   :max-id 9
   :preview-frame-type :node ;; so that we can apply keybind changes
   :types {:node ;; basic node type
           ;; TODO this will have the logic, given to us eventually by user api
           ;; that controls events and keybinds, TODO decide if it includes states

           ;; keybinds in same form as re-pressed
           {:events {:keybinds [
                                [
                                 [:edit]
                                 [{:which 85}]]
                                [
                                 [:add-child]
                                 [{:which 13}]]
                                [
                                 [:add-neighbor]
                                 [{:whcih 9}]]]}}}
   :id->data {0 {:t :node :data "root" :state "view" :children [1 2 3 4]}
              1 {:t :node :data "first task" :state "view"  :children [2]}
              2 {:t :node :data "second task" :state "view"  :children [3 4]}
              3 {:t :node :data "third task" :state "view"  :children []}
              4 {:t :node :data "fourth task" :state "view"  :children [1]}
              5 {:t :node :data "fifth task" :state "view"  :children []}
              6 {:t :node :data "sixth task" :state "view"  :children []}
              7 {:t :node :data "seventh task" :state "view"  :children []}
              8 {:t :node :data "eighth task" :state "view" :children []}
              9 {:t :node :data "ninth task" :state "edit" :children []}}})

;; https://github.com/yatesco/re-frame-stitching-together/blob/master/src/cljs/demo/views.cljs#L81
