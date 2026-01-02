(ns juce.core-test
  (:require
    [clojure.test :refer :all]
    [clojure.java.io :as io]
    [juce.core :refer :all]))

;; ------------------------------------------------------------
;; render-node tests
;; ------------------------------------------------------------

(deftest render-node-string
  (is (= "hello" (render-node "hello"))))

(deftest render-node-normal-tag
  (is (= "<div>hello</div>"
         (render-node {:tag :div
                       :attrs {}
                       :children ["hello"]
                       :void-tag? false
                       :predicate-attrs []}))))

(deftest render-node-void-tag
  (is (= "<br />"
         (render-node {:tag :br
                       :attrs {}
                       :children []
                       :void-tag? true
                       :predicate-attrs []}))))

(deftest render-node-boolean-attrs
  (is (= "<input checked />"
         (render-node {:tag :input
                       :attrs {:checked true}
                       :children []
                       :void-tag? true
                       :predicate-attrs [:checked]}))))

(deftest render-node-sequential
  (is (= "<div>A</div><div>B</div>"
         (render-node
           [(render-node {:tag :div :attrs {} :children ["A"] :void-tag? false :predicate-attrs []})
            (render-node {:tag :div :attrs {} :children ["B"] :void-tag? false :predicate-attrs []})]))))

(deftest render-node-nested-sequential
  (is (= "<span>X</span><span>Y</span><span>Z</span>"
         (render-node
           [["<span>X</span>" "<span>Y</span>"] ["<span>Z</span>"]]))))

;; ------------------------------------------------------------
;; Tag function tests
;; ------------------------------------------------------------

(deftest tag-func-simple
  (is (= "<div>hello</div>"
         (div "hello"))))

(deftest tag-func-attrs
  (is (= "<a href=\"/x\">link</a>"
         (a {:href "/x"} "link"))))

(deftest tag-func-boolean-attr
  (is (= "<input checked />"
         (input {:checked true}))))

(deftest tag-func-void-tag
  (is (= "<br />" (br))))

;; ------------------------------------------------------------
;; render tests
;; ------------------------------------------------------------

(deftest render-simple
  (is (= "<div>hello</div>"
         (render "(div \"hello\")"))))

(deftest render-with-env
  (is (= "<div>ほげ</div>"
         (render "(div (:name hoge))" {:hoge {:name "ほげ"}}))))

(deftest render-for-loop
  (is (= "<div>ほげ</div><div>ばあ</div>"
         (render "(for [item items] (div (:name item)))"
                 {:items [{:name "ほげ"} {:name "ばあ"}]}))))

(deftest render-mixed-children
  (is (= "<div>あいうえお<div>ほげ</div><div>ばあ</div>かきくけこ</div>"
         (render "(div \"あいうえお\" (for [item items] (div (:name item))) \"かきくけこ\")"
                 {:items [{:name "ほげ"} {:name "ばあ"}]}))))

;; ------------------------------------------------------------
;; render-file tests
;; ------------------------------------------------------------

(deftest render-file-test
  (let [tmp (java.io.File/createTempFile "juce-test" ".clj")]
    (spit tmp "(div \"hello-file\")")
    (is (= "<div>hello-file</div>"
           (render-file (.getPath tmp))))
    (.delete tmp)))

(deftest render-file-test-with-env
  (let [tmp (java.io.File/createTempFile "juce-test" ".clj")]
    (spit tmp "(div \"hello-file\" (p (:name hoge)))")
    (is (= "<div>hello-file<p>hoge</p></div>"
           (render-file (.getPath tmp) {:hoge {:name "hoge"}})))
    (.delete tmp)))
;; ------------------------------------------------------------
;; Edge cases
;; ------------------------------------------------------------

(deftest render-empty-string
  (is (= "" (render "\"\""))))

(deftest render-empty-list
  (is (= "" (render "()"))))

(deftest render-nil
  (is (= "" (render "nil"))))

(deftest render-number
  (is (= "42" (render "42"))))

;; ------------------------------------------------------------
;; Others
;; ------------------------------------------------------------
(deftest slurp-file-test
  (let [tmp (java.io.File/createTempFile "juce-test" ".clj")]
    (spit tmp "(div \"abc\")")
    (is (= "(div \"abc\")")
         (slurp-file (.getPath tmp)))
    (.delete tmp)))
