(ns juce.cli-test
  (:require
    [clojure.test :refer :all]
    [clojure.java.shell :as sh]
    [clojure.java.io :as io]))

;; ------------------------------------------------------------
;; Helper: run CLI and return {:out :err :exit}
;; ------------------------------------------------------------

(defn run-cli [& args]
  (apply sh/sh "clojure" "-M" "-m" "juce.cli" args))

;; ------------------------------------------------------------
;; Fixture: create/remove test/my directory
;; ------------------------------------------------------------

(defn setup-and-cleanup [tests]
  ;; Before tests
  (.mkdirs (io/file "test/my/custom"))

  ;; Run tests
  (tests)

  ;; After tests (always delete)
  (sh/sh "rm" "-rf" "test/my"))

(use-fixtures :once setup-and-cleanup)

;; ------------------------------------------------------------
;; Tests
;; ------------------------------------------------------------

(deftest test-help
  (testing "help option"
    (let [{:keys [out exit]} (run-cli "--help")]
      (is (= 0 exit))
      (is (.contains out "Render juce template language as HTML.")))))

(deftest test-expr
  (testing "-e / --expr"
    (let [{:keys [out exit]} (run-cli "-e" "(div \"hello\")")]
      (is (= 0 exit))
      (is (= "<div>hello</div>\n" out)))))

(deftest test-stdin
  (testing "stdin input"
    (let [{:keys [out exit]}
          (sh/sh "bash" "-c" "echo '(span \"yo\")' | clojure -M -m juce.cli")]
      (is (= 0 exit))
      (is (= "<span>yo</span>\n" out)))))

(deftest test-file
  (testing "-f / --file"
    (let [tmp (io/file "test-tmp.juce")]
      (spit tmp "(p \"from-file\")")
      (let [{:keys [out exit]} (run-cli "-f" (.getPath tmp))]
        (is (= 0 exit))
        (is (= "<p>from-file</p>\n" out)))
      (.delete tmp))))

;; For require test, create a temporary namespace
(def tmp-ns-path "test/my/custom/tags.clj")

(def tmp-ns-code
  "(ns my.custom.tags)
   (defn hello [] \"<span>HI</span>\")")

(deftest test-require
  (testing "-r / --require"
    ;; prepare namespace
    (spit tmp-ns-path tmp-ns-code)

    (let [{:keys [out exit]}
          (run-cli "-e" "(my.custom.tags/hello)" "-r" "my.custom.tags")]
      (is (= 0 exit))
      (is (= "<span>HI</span>\n" out)))))

(deftest test-env
  (testing "-E / --env"
    (let [{:keys [out exit]}
          (run-cli "-e" "(div (:x ctx))" "-E" "{:ctx {:x \"OK\"}}")]
      (is (= 0 exit))
      (is (= "<div>OK</div>\n" out)))))

(deftest test-unknown-option
  (testing "unknown option"
    (let [{:keys [out exit]} (run-cli "--unknown")]
      (is (= 0 exit)) ;; CLI prints help but does not exit with error
      (is (.contains out "Unknown option"))
      (is (.contains out "Render juce template language as HTML.")))))

