(ns juce.cli
  (:gen-class)
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [juce.core :as core]))

(def help-text
  "Render juce template language as HTML.

Usage:
  echo '(div \"hello\")' | juce
  juce -e '(div \"hello\")'
  juce -f template.juce
  juce -e '(m/hoge \"あいうえお\")' --require my.custom.tags/m

Options:
  -e, --expr EXPR        Evaluate EXPR instead of reading from stdin
  -f, --file FILE        Read juce DSL from FILE
  -E, --env  ENV         Provide a map for variable bindings (Clojure map syntax)
  -r, --require NS[/ALIAS]
                         Require namespace NS, optionally with ALIAS.
                         This option can be repeated. (ex. --require my.tag/m --require my.tag2/m2)
  -D  --doctype          use DOCTYPE declaration (ex. --doctype html) only html is available.
  -h, --help             Show this help message

Note:
  External tag libraries must be available on the classpath.
  (e.g. CLASSPATH=\"lib.jar\" juce --require my.tags/t ...)")

(defn read-stdin []
  (slurp *in*))

(defn parse-require [s]
  ;; "my.ns/m" → ["my.ns" "m"]
  ;; "my.ns"   → ["my.ns" nil]
  (let [[nm alias-name] (str/split s #"/")]
    [(symbol nm)
     (when alias-name (symbol alias-name))]))

(defn -main
  [& args]
  (loop [args args
         expr nil
         file nil
         env {}
         opts {}
         requires []]
    (if (empty? args)
      ;; exec DSL
      (do
        ;; namespace setting
        (binding [*ns* (the-ns 'juce.core)]
          (doseq [req requires]
            (let [[nm alias-name] (parse-require req)]
              (require nm)
              (when alias-name
                (alias alias-name nm)))))

        ;; evaluate DSL
        (let [input (cond
                      expr expr
                      file (slurp file)
                      :else (read-stdin))]
          (when-not (str/blank? input)
            (println (core/render input env opts)))
          (flush)))

      ;; parse arguments
      (let [[opt val & rest] args]
        (cond
          ;; help
          (or (= opt "-h") (= opt "--help"))
          (do (println help-text) (flush))

          ;; expr
          (or (= opt "-e") (= opt "--expr"))
          (recur rest val file env opts requires)

          ;; file
          (or (= opt "-f") (= opt "--file"))
          (recur rest expr val env opts requires)

          ;; env
          (or (= opt "-E") (= opt "--env"))
          (recur rest expr file (edn/read-string val) opts requires)

          ;; DOCTYPE
          (or (= opt "-D") (= opt "--doctype"))
          (recur rest expr file env (assoc opts :doctype true) requires)

          ;; require
          (or (= opt "-r") (= opt "--require"))
          (recur rest expr file env opts (conj requires val))

          ;; unknown
          :else
          (do
            (println "Unknown option:" opt)
            (println help-text)
            (flush)))))))

