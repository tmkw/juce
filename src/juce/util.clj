(ns juce.util
  (:require [juce.core :as j]))

(def tags
  [
   {:name "meta"      :void-tag? true  :predicate-attrs [:charset :content :http-equiv :name]}
   {:name "time"      :void-tag? false :predicate-attrs []}
   {:name "source"    :void-tag? true  :predicate-attrs [:media :sizes :srcset :type]}
   {:name "map"       :void-tag? false :predicate-attrs []}
  ])

;; stop name collision warnings temporarily.
(binding [*err* (java.io.StringWriter.)]
  (doseq [tag-info tags]
    (j/create-tag-func tag-info)))
