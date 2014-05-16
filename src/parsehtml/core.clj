(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

(def httpConnection (Jsoup/connect "http://www.allmenus.com/ny/new-york/322071-xo-cafe-and-grill/menu/"))
(def document (.get httpConnection))
(def navDivTag (.getElementById document "menu"))
(def list (.getElementsByClass navDivTag "name"))
(def menuItems (.html list))
