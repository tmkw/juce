# juce

[![Clojars Project](https://img.shields.io/clojars/v/io.github.tmkw/juce.svg)](https://clojars.org/io.github.tmkw/juce)

A small Clojure library for generating HTML using plain Clojure expressions.
It is intended for static site or page generation, and is not suitable as a dynamic web page renderer.

---

## 1. Prerequisites

Java and Clojure must be installed.

Clojure installation guide: https://clojure.org/guides/install_clojure

---

## 2. Using juce as a command line tool (CLI)

A separate CLI tool, `juce-cli`, is also available.

https://github.com/tmkw/juce-cli

---

## 3. DSL Basics

### Basic usage

```clojure
(div {:class "greeting"} (span "Hello, world!"))
```

Output:


```html
<div class="greeting"><span>Hello, world!</span></div>
```

The notation `{ .... }` represents an attribute map.

You cannot omit quotation marks as shown below, because juce follows standard Clojure syntax.

```clojure
(div {:class greeting} (span Hello, world!))
;; => error
```

juce doesn't provide DSL expression for HTML comment.
If you want to include an HTML comment, write it directly as a string:

```clojure
(html
    (div
      "<!--
         This is a comment.
       -->"
       (button "Push Me")))
```

### Attribute shorthand

Attributes can be written directly after the tag name using keyword/value pairs.

```clojure
(div :class "greeting" :id 123 "Hello")
```

The example above is equivalent to:

```clojure
(div {:class "greeting" :id 123} "Hello")
```

Attribute shorthand works only while arguments appear as keyword/value pairs.

### Mixing Clojure expressions

```clojure
(div
  (for [name ["foo" "bar"]]
    (span (str "Hello, " name))))
```

Output:

```html
<div><span>Hello, foo</span><span>Hello, bar</span></div>
```

---

## 4. Using juce as a library

Add juce to your `deps.edn`:

```
{:deps {io.github.tmkw/juce {:mvn/version "0.2.5"}}}
```

### Overview

juce provides helper functions for building HTML, so it works as a DSL as well as regular Clojure code.

### Rendering with functions

```clojure
(require '[juce.core :refer [div span]])

(div {:class "greeting"} (span "Hello, world!"))
;;=> <div class="greeting"><span>Hello, world!</span></div>
```

### Rendering from a string

```clojure
(require '[juce.core :as j])

(j/render "(div {:id \"x\"} (p \"yey!\"))")
;;=> <div id="x"><p>yey!</p></div>
```

### Rendering from a file

```clojure
(j/render-file "path/to/file")
```

### Rendering with context

```clojure
(j/render "(section (p (:name hoge)))" {:hoge {:name "HOGE"}})
;;=> <section><p>HOGE</p></section>
```

## Custom tags

juce’s built‑in tags (div, span, p, …) are ordinary Clojure functions that return HTML strings.
You can define your own tags the same way.

### Rules for custom tags

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

Custom tags can also be used inside `render` and `render-file`.

```clojure
(require '[juce.core :as j])

(j/ns-binding '[my.custom.tags :as m]
  (j/render "(m/button \"hey!\")"))
;; => <div class="my-button"><span>hey!</span></div>
```

#### External namespace loading (`ns-binding`)

`ns-binding`, a macro that temporarily loads external namespaces into `juce.core` so that custom tag functions can be used inside `render`.

```clojure
(require '[juce.core :as j])
(j/ns-binding '[my.custom.tags :as t]
  (j/render "(div (t/button \"Click me\"))"))
```

`ns-binding` temporarily loads the namespace `my.custom.tags` into `juce.core` with alias `t`,
so aliases such as `t/button` can be resolved inside `render`.

### Notes & best practices

#### 1. juce doesn't format HTML.

Output is compact. Use an external formatter if needed.

#### 2. Avoid `doseq`

`doseq` returns `nil`, so it produces no HTML. Use `for` instead.

```clojure
(require '[juce.core :as j])

(j/render "(div (doseq [n [1 2 3]] (p (str n))))")
;;=> returns <div></div>, not <div><p>1</p><p>2</p><p>3</p></div>
;;   because doseq returns nil.

(j/render "(div (for [n [1 2 3]] (p n)))")
;;=> returns <div><p>1</p><p>2</p><p>3</p></div>
```

#### 3. Some HTML tags conflict with Clojure core

Use them from the `juce.util` namespace.

- meta
- time
- source
- map

It is recommended to use them with a namespace qualifier to avoid name collisions.

```clojure
(require '[juce.core :as j :refer [div p]]
         '[juce.util :as u])
(div
  (p "abcde")
  (u/time {:datetime "2026-01-02"} "1 Jan 2026"))
```

---

## 4. Intended Use / Safety Notes

juce evaluates Clojure expressions directly.
Use it with trusted, developer-authored templates such as static site content.
It is not intended for rendering untrusted input in web applications.

---

## 5. License

juce is distributed under the BSD 2-Clause License (SPDX: BSD-2-Clause).
See the LICENSE file for details.

---

## 6. Author

Takanobu Maekawa

https://github.com/tmkw
