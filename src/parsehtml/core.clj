(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

(def ^org.jsoup.Connection httpConnection (Jsoup/connect "http://www.allmenus.com/ny/new-york/322071-xo-cafe-and-grill/menu/"))
(def ^org.jsoup.nodes.Document document (.get httpConnection))
(def ^org.jsoup.nodes.Element navDivTag (.getElementById document "menu"))
(def ^org.jsoup.select.Elements elements (.getElementsByClass navDivTag "name"))
(def ^String menuItems (.html elements))
(doseq [element elements]

  (prn (.html element)))
 
(for [element elements] element)

(dotimes [element (.size elements)] (prn element))

