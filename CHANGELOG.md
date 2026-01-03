# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.0] - 2026-01-03
### Added
- Added `ns-binding` macro for loading external namespaces into `juce.core` with alias support.
  This enables seamless use of custom tag functions defined outside juce.
  Example:
  ```clojure
  (ns-binding '[my.custom.tags :as t]
    (render "(div (t/button \"Click me\"))"))
  ```
- Added attribute shorthand syntax.
  Attributes can now be written directly after the tag name using keyword/value pairs:
  ```clojure
  (div :class "abc" :id 123 "hello")
  ```
  Maps appearing anywhere in the argument list are merged into the attribute map:
  ```clojure
  (div :class "abc" "hello" {:id "x"})
  ```

## [0.1.1] - 2026-01-03
### Added
- `render-file` now accepts an optional context map as the second argument.

## [0.1.0] - 2026-01-02
### Added
- Initial public release of **juce**, an HTML DSL for Clojure programmers.
- Core DSL for generating HTML using pure Clojure expressions.
- Support for attributes, nested tags, and seamless integration with Clojure control flow.
- `render` function for evaluating juce DSL expressions from strings.
- `render-file` function for evaluating juce DSL expressions from files.
- CLI support for evaluating juce DSL expressions via `-e` option.
- `juce.util` namespace providing tag helpers to avoid name collisions (`meta`, `time`, `source`, `map`).

