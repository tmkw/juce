(ns juce.core
  (:require
    [juce.tags :as t]
    [clojure.java.io :as io]))


(defn slurp-file
  [path]
  (slurp (io/file path)))

(defn render-attrs [attrs predicate-attrs]
  (apply str
    (for [[k v] attrs]
      (cond
        ;; boolean attribute: true
        (and (contains? (set predicate-attrs) k) (= v true))
          (str " " (name k))
        ;; boolean attribute: false => do not output
        (and (contains? (set predicate-attrs) k) (= v false))
          ""
        ;; normal attribute =>  name="value"
        :else
          (str " " (name k) "=\"" v "\"")))))

(defn render-node [node]
  (cond
    (string? node)
      node
    (clojure.core/map? node)
      (let [{:keys [tag attrs children void-tag? predicate-attrs]} node]
        (if void-tag?
          (str "<" (name tag)
               (render-attrs attrs predicate-attrs)
               " />")
          (str "<" (name tag)
               (render-attrs attrs predicate-attrs)
               ">"
               (apply str (map render-node children))
               "</" (name tag) ">")))
    (sequential? node)
      (apply str (map render-node node))
    :else
      (str node)))

(defn create-tag-func [tag-info]
  (let [tag-name (:name tag-info)
        void-tag? (:void-tag? tag-info)
        predicate-attrs (:predicate-attrs tag-info)]
    (eval
      `(def
        ~(symbol tag-name)
        (fn [& args#]
          (let [attrs#     (apply merge (filter map? args#))
                children#  (remove map? args#)]
            (render-node
              {:tag      ~(keyword tag-name)
               :attrs    attrs#
               :children children#
               :void-tag? ~void-tag?
               :predicate-attrs ~predicate-attrs})))))))


;; generate tag functions
(doseq [tag t/tags]
  (create-tag-func tag))


(defn render
  "Evaluates a juce template string with an optional environment map and returns the rendered HTML."
  ([s]
   (render s {}))
  ([s env]
   (binding [*ns* (the-ns 'juce.core)]
     (let [form   (read-string s)
           result (eval
                    `(let [~@(mapcat (fn [[k v]] [(symbol (name k)) v]) env)]
                       ~form))]
       (render-node result)))))


(defn render-file
  "Reads a juce template file and returns the rendered HTML."
  ([path]
    (render (slurp-file path) {}))
  ([path env]
    (render (slurp-file path) env)))

