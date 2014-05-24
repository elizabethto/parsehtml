(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

(def ^org.jsoup.Connection httpConnection (Jsoup/connect "http://www.allmenus.com/ny/new-york/322071-xo-cafe-and-grill/menu/"))
(def ^org.jsoup.nodes.Document document (.get httpConnection))
(def ^org.jsoup.nodes.Element navDivTag (.getElementById document "menu"))
(def ^org.jsoup.select.Elements fullMenu (.children navDivTag))
(def ^clojure.lang.IteratorSeq seqFullMenu (seq fullMenu))

;; Creates a list of the menu categories i.e.
;;["Appetizers" "Dim Sum, Snacks &amp; Appetizers" "Soups"]
(def categoryList [])

(doseq [element seqFullMenu]
  (def findCategory (.select element "h3"))
  (def categoryName (.html findCategory))
  (def categoryList (conj categoryList categoryName)))

;; Puts the menu categories into a list of a map of keywords i.e.
;;[{:name "Appetizers"}{:name "Dim Sum, Snacks &amp; Appetizers"} {:name "Soups"}]
(def categoryMap {})
(def categoryNameList [])

(doseq [element categoryList]
  (def categoryMap {(keyword element) nil})
  (def categoryNameList (conj categoryNameList categoryMap)))

;; creates the whole menu with categories and category items and prices
(dotimes [i (.size seqFullMenu)]
  (def getCategory (nth seqFullMenu i))
;;create item name list for category
  (def nameElements (.getElementsByClass getCategory "name"))
  (def itemNameList [])
  (doseq [element nameElements]
    (def itemNameList (conj itemNameList (.html element)))
    (prn itemNameList))
;;create item price list for category
  (def priceElements (.getElementsByClass getCategory "price"))
  (def itemPriceList [])
  (doseq [element priceElements]
    (def tempStrPrice (subs (.html element) 1))
    (def intPrice (Double/parseDouble tempStrPrice))
    (def itemPriceList (conj itemPriceList intPrice))
    (prn itemPriceList))
;;Convert name to keyword values
  (def tempMap {})
  (def itemNameMap [])

  (doseq [element itemNameList]
    (def tempMap (assoc tempMap :name element))
    (def itemNameMap (conj itemNameMap tempMap)))

;;Convert price to keyword values
  (def tempMap {})
  (def itemPriceMap [])

  (doseq [element itemPriceList]
    (def tempMap (assoc tempMap :price element))
      (def itemPriceMap (conj itemPriceMap tempMap)))

;;Create a map of name and price for Appetizer category
  (def menuMap {})
  (def menuMap (zipmap itemNameMap itemPriceMap))
  (def menuMap (map list (keys menuMap) (vals menuMap)))
  (def finalMenu [])

  (doseq [item menuMap]
    (def tempItem (merge (last item) (first item)))
        (def categoryMenu (conj categoryMenu tempItem)))


  (prn "HELLO WORLD!!!!"))

;;old code below

;;Create the list of itemNames i.e.
;;["Grilled Salmon Fish Head" "Grilled Whole Squid with Special Sauce" "Pan Fried Dumpling"]
(def ^org.jsoup.nodes.Element appCategory (nth seqFullMenu 0))
(def ^org.jsoup.select.Elements nameElements (.getElementsByClass appCategory "name"))

(def itemNameList [])

(doseq [element nameElements]
  (def itemNameList (conj itemNameList (.html element))))

;;Create the list of itemPrices i.e.
;;["$6.95" "$6.95" "$3.95"]
(def ^org.jsoup.select.Elements priceElements (.getElementsByClass appCategory "price"))

(def itemPriceList [])

(doseq [element priceElements]
  (def tempStrPrice (subs (.html element) 1))
  (def intPrice (Double/parseDouble tempStrPrice))
  (def itemPriceList (conj itemPriceList intPrice)))

;;Convert name to keyword values
;;[{:name "Grilled Salmon Fish Head"} {:name "Grilled Whole Squid}]
(def tempMap {})
(def itemNameMap [])

(doseq [element itemNameList]
  (def tempMap (assoc tempMap :name element))
  (def itemNameMap (conj itemNameMap tempMap)))

;;Convert price to keyword values
;;[{:price "$6.95"} {:price "$6.95"} {:price "$3.95"}]
(def tempMap {})
(def itemPriceMap [])

(doseq [element itemPriceList]
  (def tempMap (assoc tempMap :price element))
  (def itemPriceMap (conj itemPriceMap tempMap)))

;;Create a map of name and price for Appetizer category
(def menuMap {})
(def menuMap (zipmap itemNameMap itemPriceMap))
(def menuMap (map list (keys menuMap) (vals menuMap)))
(def finalMenu [])

(doseq [item menuMap]
  (def tempItem (merge (last item) (first item)))
    (def finalMenu (conj finalMenu tempItem)))

;;scratch
(def foo ( categoryNameList 0))
   {:Appetizers nil}

(assoc-in foo [:Appetizers] finalMenu)





