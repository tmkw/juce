# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.5] - 2026-01-11
### Added
- Added doctype options to the render function
- Added `-D` / `--doctype` option to the CLI, allowing users to specify if DOCTYPE is needed or not.

## [0.2.4] - 2026-01-07
### Added
- Added `-f` / `--file` option to the CLI, allowing users to render juce DSL expressions from a file.

## [0.2.3] - 2026-01-05
### Removed
- Removed the `juce` command-line script from the `bin` directory.
  It has been replaced by the `juce-cli` project: https://github.com/tmkw/juce-cli
### Changed
- Improved the CLI help message.

## [0.2.2] - 2026-01-04
### Added
- Added a command line tool named `juce` in `bin` directory.
- Added `juce.version` namespace, automatically generated during the build process.
  This allows users to check the library version directly from the REPL:
  ```clojure
  (require 'juce.version)
  juce.version/version
  ```

## [0.2.1] - 2026-01-03
### Changed
- No functional changes; this release focuses solely on documentation quality and internal clarity.

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

