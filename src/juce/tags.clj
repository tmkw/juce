(ns juce.tags)

(def tags
  [
   ;; Document metadata
   {:name "html"      :void-tag? false :predicate-attrs []}
   {:name "head"      :void-tag? false :predicate-attrs []}
   {:name "body"      :void-tag? false :predicate-attrs []}
   {:name "title"     :void-tag? false :predicate-attrs []}
   {:name "base"      :void-tag? true  :predicate-attrs []}
   {:name "link"      :void-tag? true  :predicate-attrs [:disabled]}
   {:name "style"     :void-tag? false :predicate-attrs []}
   {:name "script"    :void-tag? false :predicate-attrs [:async :defer :nomodule]}
   {:name "noscript"  :void-tag? false :predicate-attrs []}
   {:name "template"  :void-tag? false :predicate-attrs []}

   ;; Sections
   {:name "header"    :void-tag? false :predicate-attrs []}
   {:name "footer"    :void-tag? false :predicate-attrs []}
   {:name "nav"       :void-tag? false :predicate-attrs []}
   {:name "section"   :void-tag? false :predicate-attrs []}
   {:name "article"   :void-tag? false :predicate-attrs []}
   {:name "aside"     :void-tag? false :predicate-attrs []}
   {:name "main"      :void-tag? false :predicate-attrs []}
   {:name "h1"        :void-tag? false :predicate-attrs []}
   {:name "h2"        :void-tag? false :predicate-attrs []}
   {:name "h3"        :void-tag? false :predicate-attrs []}
   {:name "h4"        :void-tag? false :predicate-attrs []}
   {:name "h5"        :void-tag? false :predicate-attrs []}
   {:name "h6"        :void-tag? false :predicate-attrs []}
   {:name "address"   :void-tag? false :predicate-attrs []}

   ;; Text content
   {:name "p"         :void-tag? false :predicate-attrs []}
   {:name "hr"        :void-tag? true  :predicate-attrs []}
   {:name "pre"       :void-tag? false :predicate-attrs []}
   {:name "blockquote":void-tag? false :predicate-attrs []}
   {:name "ol"        :void-tag? false :predicate-attrs [:reversed]}
   {:name "ul"        :void-tag? false :predicate-attrs []}
   {:name "li"        :void-tag? false :predicate-attrs []}
   {:name "dl"        :void-tag? false :predicate-attrs []}
   {:name "dt"        :void-tag? false :predicate-attrs []}
   {:name "dd"        :void-tag? false :predicate-attrs []}
   {:name "figure"    :void-tag? false :predicate-attrs []}
   {:name "figcaption":void-tag? false :predicate-attrs []}
   {:name "div"       :void-tag? false :predicate-attrs []}

   ;; Inline text
   {:name "a"         :void-tag? false :predicate-attrs [:download]}
   {:name "em"        :void-tag? false :predicate-attrs []}
   {:name "strong"    :void-tag? false :predicate-attrs []}
   {:name "small"     :void-tag? false :predicate-attrs []}
   {:name "s"         :void-tag? false :predicate-attrs []}
   {:name "cite"      :void-tag? false :predicate-attrs []}
   {:name "q"         :void-tag? false :predicate-attrs []}
   {:name "dfn"       :void-tag? false :predicate-attrs []}
   {:name "abbr"      :void-tag? false :predicate-attrs []}
   {:name "ruby"      :void-tag? false :predicate-attrs []}
   {:name "rt"        :void-tag? false :predicate-attrs []}
   {:name "rp"        :void-tag? false :predicate-attrs []}
   {:name "data"      :void-tag? false :predicate-attrs []}
   {:name "code"      :void-tag? false :predicate-attrs []}
   {:name "var"       :void-tag? false :predicate-attrs []}
   {:name "samp"      :void-tag? false :predicate-attrs []}
   {:name "kbd"       :void-tag? false :predicate-attrs []}
   {:name "sub"       :void-tag? false :predicate-attrs []}
   {:name "sup"       :void-tag? false :predicate-attrs []}
   {:name "i"         :void-tag? false :predicate-attrs []}
   {:name "b"         :void-tag? false :predicate-attrs []}
   {:name "u"         :void-tag? false :predicate-attrs []}
   {:name "mark"      :void-tag? false :predicate-attrs []}
   {:name "span"      :void-tag? false :predicate-attrs []}
   {:name "br"        :void-tag? true  :predicate-attrs []}
   {:name "wbr"       :void-tag? true  :predicate-attrs []}
   {:name "bdi"       :void-tag? false :predicate-attrs []}
   {:name "bdo"       :void-tag? false :predicate-attrs []}
   {:name "ins"       :void-tag? false :predicate-attrs []}
   {:name "del"       :void-tag? false :predicate-attrs []}

   ;; Media
   {:name "img"       :void-tag? true  :predicate-attrs [:ismap]}
   {:name "picture"   :void-tag? false :predicate-attrs []}
   {:name "iframe"    :void-tag? false :predicate-attrs [:allowfullscreen]}
   {:name "embed"     :void-tag? true  :predicate-attrs []}
   {:name "object"    :void-tag? false :predicate-attrs []}
   {:name "param"     :void-tag? true  :predicate-attrs []}
   {:name "video"     :void-tag? false :predicate-attrs [:autoplay :controls :loop :muted :playsinline]}
   {:name "audio"     :void-tag? false :predicate-attrs [:autoplay :controls :loop :muted]}
   {:name "track"     :void-tag? true  :predicate-attrs [:default]}
   {:name "area"      :void-tag? true  :predicate-attrs [:download :nofollow :noopener :noreferrer]}
   {:name "canvas"    :void-tag? false :predicate-attrs []}
   {:name "svg"       :void-tag? false :predicate-attrs []}

   ;; Table
   {:name "table"     :void-tag? false :predicate-attrs []}
   {:name "caption"   :void-tag? false :predicate-attrs []}
   {:name "colgroup"  :void-tag? false :predicate-attrs []}
   {:name "col"       :void-tag? true  :predicate-attrs []}
   {:name "thead"     :void-tag? false :predicate-attrs []}
   {:name "tbody"     :void-tag? false :predicate-attrs []}
   {:name "tfoot"     :void-tag? false :predicate-attrs []}
   {:name "tr"        :void-tag? false :predicate-attrs []}
   {:name "td"        :void-tag? false :predicate-attrs []}
   {:name "th"        :void-tag? false :predicate-attrs []}

   ;; Forms
   {:name "form"      :void-tag? false :predicate-attrs [:novalidate]}
   {:name "label"     :void-tag? false :predicate-attrs []}
   {:name "input"     :void-tag? true  :predicate-attrs [:checked :disabled :readonly :required :autofocus :multiple]}
   {:name "button"    :void-tag? false :predicate-attrs [:disabled :autofocus]}
   {:name "select"    :void-tag? false :predicate-attrs [:multiple :disabled :required]}
   {:name "datalist"  :void-tag? false :predicate-attrs []}
   {:name "optgroup"  :void-tag? false :predicate-attrs [:disabled]}
   {:name "option"    :void-tag? false :predicate-attrs [:selected :disabled]}
   {:name "textarea"  :void-tag? false :predicate-attrs [:disabled :readonly :required]}
   {:name "output"    :void-tag? false :predicate-attrs []}
   {:name "progress"  :void-tag? false :predicate-attrs []}
   {:name "meter"     :void-tag? false :predicate-attrs []}
   {:name "fieldset"  :void-tag? false :predicate-attrs [:disabled]}
   {:name "legend"    :void-tag? false :predicate-attrs []}

   ;; Interactive
   {:name "details"   :void-tag? false :predicate-attrs [:open]}
   {:name "summary"   :void-tag? false :predicate-attrs []}
   {:name "dialog"    :void-tag? false :predicate-attrs [:open]}
   {:name "menu"      :void-tag? false :predicate-attrs []}
   {:name "menuitem"  :void-tag? false :predicate-attrs [:checked :disabled]}

   ;; Web components
   {:name "slot"      :void-tag? false :predicate-attrs []}
  ])

