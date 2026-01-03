# juce
[![Clojars Project](https://img.shields.io/clojars/v/io.github.tmkw/juce.svg)](https://clojars.org/io.github.tmkw/juce)

HTML DSL for Clojure programmers. It looks like Just Clojure Expression.

## Overview
juce is a domain-specific language (DSL) for generating HTML using pure Clojure expressions.
In fact, juce provides helper functions for generating HTML, so it is both DSL and Clojure expression at the same time.

## Prerequisites
Java and Clojure must be installed.

## Usage

### Using as a library
Add juce as a dependency in your `deps.edn`:
```
{:deps {io.github.tmkw/juce {:mvn/version "0.2.0"}}}
```
Then use it in your code.
```clojure
(require '[juce.core :refer [div span]])
(div {:class "greeting"} (span "Hello, world!"))
```
Both `div` and `span` are functions that return HTML tag expressions.
So the above expression results in:
```html
<div class="greeting"><span>Hello, world!</span></div>
```

#### Mixing Clojure expressions
You can mix Clojure expressions seamlessly.
```clojure
(let [name "hoge"]
  (div
    (span
      (if (nil? name) 
        "Hello, world!"
        (str "Hello, " name)))))
```
This produces:
```html
<div><span>Hello, hoge</span></div>
```

Here is another example.
```clojure
(require '[juce.core :refer [div p]])
(let [people [{:name "John" :phone "xxx-xxxxx"}
              {:name "Tom"  :phone "yyy-yyyyy"}]]
  (div
    (for [person people]
      (p (str (:name person) " " (:phone person))))))
```
This produces:
```html
<div><p>John xxx-xxxxx</p><p>Tom yyy-yyyyy</p></div>
```

#### Render functions
If you want to render juce DSL expression from a string, you can use `render` function.
```clojure
(require '[juce.core :as j])
(j/render "(div {:class \"abc\" :id \"123\"} (p \"yey!\"))")
```
This produces:
```html
<div class="abc" id="123"><p>yey!</p></div>
```

If you want to render juce DSL expression from a file, `render-file` is available.
```clojure
(require '[juce.core :as j])
(j/render-file "path/to/file")
```

#### Rendering with context data
Both `render` and `render-file` accept context data as the second argument.
For example:
```clojure
(require '[juce.core :as j])
(j/render
  "(section {:id \"x\"} (p (:name hoge)))"
  {:hoge {:name "ほげ"}})
```
This produces:
```html
<section id="x"><p>ほげ</p></section>
```

#### Attribute shorthand syntax
Starting from juce 0.2.0, attributes can be written directly after the tag name using keyword/value pairs:
```clojure
(div :class "greeting" :id 123 "Hello")
```
This is equivalent to:
```clojure
(div {:class "greeting" :id 123} "Hello")
```
Once a non-keyword argument appears twice in a row, the rest of the arguments are treated as normal child elements.

#### External namespace loading (`ns-binding`)
juce 0.2.0 introduces `ns-binding`, a macro that temporarily loads external namespaces into `juce.core` so that custom tag functions can be used inside `render`.

For example:
```clojure
(require '[juce.core :as j])
(j/ns-binding '[my.custom.tags :as t]
  (j/render "(div (t/button \"Click me\"))"))
```
`ns-binding` temporarily loads the namespace `my.custom.tags` into `juce.core` with alias `t`,
so aliases such as `t/button` can be resolved inside `render`.

### Using juce as a CLI
1. `git clone` this repository.
2. clojure -T:build jar
3. target/juce-x.x.x.jar is generated.
4. add juce-x.x.x.jar to the CLASSPATH
5. make a script file and set it executable:
```shell
$ echo 'clojure -M:cli "$@"' > juce
$ chmod +x juce
```
Then, you can use 'juce' command. For example:
```shell
$ CLASSPATH="./target/juce-0.2.0.jar" ./juce -e '(div "abcdefg")'
<div>abcdefg</div>
```
If you add `juce` command to the PATH, `./` doesn't need
```shell
$ CLASSPATH="./target/juce-0.2.0.jar" juce -e '(div "abcdefg")'
```

### Note
1. juce doesn't format HTML.
2. using `doseq` is NOT recommended because it returns `nil`. Use `for` instead.
```clojure
(require '[juce.core :as j])

(j/render "(div (doseq [n [1 2 3]] (p (str n))))")
;;=> returns <div></div>, not <div><p>1</p><p>2</p><p>3</p></div>
;;   because doseq returns nil.

(j/render "(div (for [n [1 2 3]] (p n)))")
;;=> returns <div><p>1</p><p>2</p><p>3</p></div>
```
3. To avoid name collisions with Clojure core or reserved symbols, the following HTML tags are provided via the `juce.util` namespace:
  - meta
  - time
  - source
  - map

It is recommended to use them with a namespace qualifier.
```clojure
(require '[juce.core :as j :refer [div p]]
         '[juce.util :as u])
(div
  (p "abcde")
  (u/time {:datetime "2026-01-02"} "1 Jan 2026"))
```


## License
juce is distributed under the BSD 2-Clause "Simplified" License (SPDX: BSD-2-Clause).
See the LICENSE file for details.

