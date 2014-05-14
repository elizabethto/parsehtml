(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

(def httpConnection (Jsoup/connect "http://www.allmenus.com/ny/new-york/250138-xo-kitchen/menu/"))
(def document (.get httpConnection))
(def links (.select document "#menu"))

(println 1)
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
