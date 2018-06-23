(ns ^:figwheel-no-load front.dev
  (:require
    [front.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
