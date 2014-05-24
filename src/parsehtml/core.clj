(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

(def ^org.jsoup.Connection httpConnection (Jsoup/connect "http://www.allmenus.com/ny/new-york/322071-xo-cafe-and-grill/menu/"))
(def ^org.jsoup.nodes.Document document (.get httpConnection))
(def ^org.jsoup.nodes.Element navDivTag (.getElementById document "menu"))

(def ^org.jsoup.select.Elements nameElements (.getElementsByClass navDivTag "name"))
(def ^org.jsoup.select.Elements priceElements (.getElementsByClass navDivTag "price"))

(def menu [])
(doseq [element nameElements]
  (def menu (conj menu (.html element))))

(def price [])
(doseq [element priceElements]
  (def price (conj price (.html element))))

(def menuMap {})
(def menuList [])

(doseq [menuElement menu]
  (def menuMap (assoc menuMap :name menuElement))
  (def menuList (conj menuList menuMap)))

(def priceMap {})
(def priceList [])

(doseq [priceElement price]
  (def priceMap (assoc priceMap :price priceElement))
  (def priceList (conj priceList priceMap)))

(def menuMap {})

(def menuMap (zipmap menuList priceList))

(def menuMap (map list (keys menuMap) (vals menuMap)))

(def finalMenu [])

(doseq [item menuMap]
  (def tempItem (merge (last item) (first item)))
  (def finalMenu (conj finalMenu tempItem)))

(spit "menu.txt" (apply str finalMenu))


{:appetizers [{:name "salmon" :price 10.95} {:name "chicken wings" :price 6.95}] :dimsum [{:name "shrimp dumpling" :price 3.95} {:name "turnip cake" :price 1.95}] :beverages [{:name "soda" :price 1.25} {:name "green tea" :price 1.25}] }
