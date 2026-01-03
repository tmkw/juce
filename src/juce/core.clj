(ns juce.core
  (:require
    [juce.tags :as t]
    [clojure.java.io :as io]))

(defmacro ns-binding
  "Temporarily loads one or more external namespaces into `juce.core` and evaluates the given body in that context.

  Usage:
    (ns-binding '[my.tags :as t]
      (render \"(div (t/button \\\"Click\\\"))\"))

  Parameters:
    requires-and-body - A sequence where all but the last element are `require` forms (vectors),
                       and the last element is the body expression to evaluate.

  Behavior:
    - Each require form is evaluated with `*ns*` bound to `juce.core`.
    - The body is then evaluated in the same namespace.
    - Useful for making custom tag functions available inside `render`.

  Returns:
    The result of evaluating the body expression."
  [& requires-and-body]
  (let [requires (butlast requires-and-body)
        body     (last requires-and-body)]
    `(do
       ;; juce.core に require を追加
       (binding [*ns* (the-ns 'juce.core)]
         ~@(for [req requires]
             `(require ~req)))

       ;; body を juce.core の文脈で評価
       (binding [*ns* (the-ns 'juce.core)]
         ~body))))

(defn render-attrs [attrs predicate-attrs]
  "Internal helper. Converts an attribute map into an HTML attribute string.
   Handles boolean attributes listed in `predicate-attrs`."
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
  "Internal helper. Recursively renders a juce node into an HTML string.

   Accepts:
    - string
    - map {:tag kw :attrs map :children seq :void-tag? bool}
    - sequential (vector/list of nodes)

  Returns:
    HTML string."
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

(defn parse-args [args]
  "Internal helper. Parses tag function arguments.

  Accepts:
    args - A sequence of:
      - keyword/value pairs (attribute shorthand)
      - maps (merged into attributes)
      - any other values (treated as children)

  Returns:
    {:attrs map :children vector}"
  (loop [xs args attrs {} children [] mode nil]
    (if (empty? xs)
      {:attrs attrs :children children}
      (let [x (first xs)]
        (cond
          (and (nil? mode) (keyword? x))
            (recur xs attrs children :attrs)
          (and (nil? mode) (not (keyword? x)))
            (recur xs attrs children :children)
          (and (= mode :attrs) (keyword? x))
            (let [k x v (second xs)]
              (recur (nnext xs) (assoc attrs k v) children :attrs))
          (and (= mode :attrs) (not (keyword? x)))
            (recur (rest xs) attrs (conj children x) :children)
          (and (= mode :children) (map? x))
          (recur (rest xs) (merge attrs x) children :children)
          :else
            (recur (rest xs) attrs (conj children x) :children))))))

(defn create-tag-func [tag-info]
  "Internal. Generates a tag function such as `div`, `span`, etc."
  (let [tag-name (:name tag-info)
        void-tag? (:void-tag? tag-info)
        predicate-attrs (:predicate-attrs tag-info)]
    (eval
      `(def
        ~(symbol tag-name)
        (fn [& args#]
          (let [{attrs# :attrs children# :children} (parse-args args#)]
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
  "Evaluates a juce template string and returns the rendered HTML.

   Usage:
     (render \"(div :class \\\"x\\\" \\\"Hello\\\")\")
     (render \"(p (:name user))\" {:user {:name \"Alice\"}})

   Parameters:
     s   - String containing a juce DSL expression.
     env - Optional map. Keys become local symbols inside the template.

   Behavior:
     - The template string is read as a Clojure form.
     - `env` bindings are injected as locals.
     - The form is evaluated in the `juce.core` namespace.
     - The result is converted to HTML via `render-node`.

   Returns:
     HTML string."
  ([s]
   (render s {}))
  ([s env]
   (binding [*ns* (the-ns 'juce.core)]
     (let [form   (read-string s)
           result (eval
                    `(let [~@(mapcat (fn [[k v]] [(symbol (name k)) v]) env)]
                       ~form))]
       (render-node result)))))

(defn slurp-file
  "Internal. Reads a file from disk and returns its contents as a string."
  [path]
  (slurp (io/file path)))

(defn render-file
  "Reads a juce template file and returns the rendered HTML.

   Usage:
     (render-file \"template.juce\")
     (render-file \"template.juce\" {:name \"Alice\"})

   Parameters:
     path - Path to a file containing a juce DSL expression.
     env  - Optional map of local bindings.

   Returns:
     HTML string."
  ([path]
    (render (slurp-file path) {}))
  ([path env]
    (render (slurp-file path) env)))

