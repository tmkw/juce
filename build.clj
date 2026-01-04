(ns build
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'io.github.tmkw/juce)
(def version (clojure.string/trim (slurp "VERSION")))


(def class-dir "target/classes")
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (clean nil)
  (let [basis (b/create-basis {})]
    (b/write-pom
      {:class-dir class-dir
       :lib lib
       :version version
       :basis basis
       :pom-data
        [[:description "HTML DSL for Clojure programmers"]
         [:url "https://github.com/tmkw/juce"]
         [:licenses
           [:license
            [:name "BSD 2-Clause License"]
            [:url "https://opensource.org/licenses/BSD-2-Clause"]]]
         [:scm
           [:url "https://github.com/tmkw/juce"]
           [:connection "scm:git:git://github.com/tmkw/juce.git"]
           [:developerConnection "scm:git:ssh://git@github.com/tmkw/juce.git"]]]})


    (b/copy-dir {:src-dirs ["src"]
                 :target-dir class-dir})

    (b/jar {:class-dir class-dir
            :jar-file jar-file})))

(defn deploy [_]
  (jar nil)
  (dd/deploy {:installer :remote
              :artifact jar-file
              :pom-file (b/pom-path {:class-dir class-dir
                                     :lib lib
                                     :version version})}))
(defn cli [_]
  (let [bin-dir "bin"
        script (str bin-dir "/juce")
        content (format "clojure -Sdeps '{:deps {io.github.tmkw/juce {:mvn/version \"%s\"}}}' -M:cli \"$@\"" version)]
    (.mkdirs (java.io.File. bin-dir))
    (spit script content)
    (println "CLI script generated at:" script)))

(defn release [_]
  (deploy nil)
  (cli nil)
  (println "Release completed for version" version))


