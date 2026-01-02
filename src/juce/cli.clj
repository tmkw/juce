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
  juce -e '(m/hoge \"あいうえお\")' --require my.custom.tags/m

Options:
  -e, --expr EXPR        Evaluate EXPR instead of reading from stdin
  -E, --env  ENV         Provide EDN environment map for variable bindings
  -r, --require NS[/ALIAS]
                         Require namespace NS, optionally with ALIAS
  -h, --help             Show this help message")

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
         env {}
         requires []]
    (if (empty? args)
      ;; 実行フェーズ
      (do
        (binding [*ns* (the-ns 'juce.core)]
          (doseq [req requires]
            (let [[nm alias-name] (parse-require req)]
              (require nm)
              (when alias-name
                (alias alias-name nm)))))

        ;; DSL を評価
        (let [input (or expr (read-stdin))]
          (when-not (str/blank? input)
            (println (core/render input env)))
          (flush)))

      ;; パースフェーズ
      (let [[opt val & rest] args]
        (cond
          ;; help
          (or (= opt "-h") (= opt "--help"))
          (do (println help-text) (flush))

          ;; expr
          (or (= opt "-e") (= opt "--expr"))
          (recur rest val env requires)

          ;; env
          (or (= opt "-E") (= opt "--env"))
          (recur rest expr (edn/read-string val) requires)

          ;; require
          (or (= opt "-r") (= opt "--require"))
          (recur rest expr env (conj requires val))

          ;; unknown
          :else
          (do
            (println "Unknown option:" opt)
            (println help-text)
            (flush)))))))

