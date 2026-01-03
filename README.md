# juce
[![Clojars Project](https://img.shields.io/clojars/v/io.github.tmkw/juce.svg)](https://clojars.org/io.github.tmkw/juce)

HTML DSL for Clojure programmers. It looks like Just Clojure Expression.

## Overview
juce is a domain‑specific language (DSL) for generating HTML using pure Clojure expressions.
It provides helper functions for building HTML, so it works both as a DSL and as regular Clojure code.

## Prerequisites
Java and Clojure must be installed.

## Usage
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
You can freely mix regular Clojure expressions with juce tags.
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
If you want to render a juce DSL expression from a string, you can use `render` function.
```clojure
(require '[juce.core :as j])
(j/render "(div {:class \"abc\" :id \"123\"} (p \"yey!\"))")
```
This produces:
```html
<div class="abc" id="123"><p>yey!</p></div>
```

If you want to render a juce DSL expression from a file, `render-file` is available.
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
(div :class "greeting" :id 123 "Hello" "World")
;; class "greeting" -> attribute
;; id    123        -> attribute
;; "Hello" and "World" ->  child elements
```
This is equivalent to:
```clojure
(div {:class "greeting" :id 123} "Hello")
```
Attribute shorthand is parsed only while arguments form keyword/value pairs.

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

### Custom Tags
juce’s built‑in tags (`div`, `span`, `p`, etc) are just Clojure functions.
You can define your own custom tags in exactly the same way.
A custom tag is simply a Clojure function that returns HTML.

#### Rules for custom tags
- Define a Clojure function whose name represents the tag or component you want to create.
  Examples: `button`, `card`, `hero-section`, etc.

- The function may take any number or shape of arguments.
  juce does not enforce the interface of custom tags.

- The function must return a valid HTML string.
  This is the only requirement. juce’s rendering pipeline expects HTML as the final output.

- You may freely use juce’s tag functions inside your custom tag, or you can also generate HTML manually.

```clojure
(ns my.custom.tags
  (:require [juce.core :as j]))

(defn button [label]
  (j/div :class "my-button"
    (j/span label)))
```
You can also implement custom tags without using juce at all.
The following example is equivalent to the above one.
```clojure
(ns my.custom.tags)

(defn button [label]
  (str "<div class=\"my-button\"><span>" label "</span></div>"))
```
Both versions work as expected:
```clojure
(require '[my.custom.tags :as m] '[juce.core :as j :refer [section]])

(m/button "hey!")
;; => <div class="my-button"><span>hey!</span></div>

(section (m/button "Hi!"))
;; => <section><div class="my-button"><span>Hi!</span></div></section>
```
Custom tags can also be used inside `render` (or `render-file`).
```clojure
(require '[juce.core :as j])

(j/ns-binding '[my.custom.tags :as m]
  (j/render "(m/button \"hey!\")"))
;; => <div class="my-button"><span>hey!</span></div>
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

## Using juce as a CLI
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

## License
juce is distributed under the BSD 2-Clause "Simplified" License (SPDX: BSD-2-Clause).
See the LICENSE file for details.

