(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

(def httpConnection (Jsoup/connect "http://jsoup.org"))
(def document (.get httpConnection))
(def navDivTag (.getElementsByClass document "nav-sections"))

(def dummy (.get navDivTag 0))

(def list (.getElementsByTag dummy "a"))

(def topics (.html list))
