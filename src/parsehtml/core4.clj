(ns parsehtml.core)
(import 'org.jsoup.Jsoup)

;; Creates a list of the menu categories i.e.
;;["Appetizers" "Dim Sum, Snacks &amp; Appetizers" "Soups"]
(defn getCategory [seqFullMenu]
  (def categoryList [])

  (doseq [element seqFullMenu]
    (def findCategory (.select element "h3"))
    (def categoryName (.html findCategory))
    (def categoryList (conj categoryList categoryName)))
  categoryList)

;; Puts the menu categories into a list of a map of keywords i.e.
;;[{:name "Appetizers"}{:name "Dim Sum, Snacks &amp; Appetizers"} {:name "Soups"}]
(defn makeKeyword [listToChange]
  (def tempMap {})
  (def tempList [])

  (doseq [element listToChange]
    (def tempMap {(keyword element) nil})
    (def tempList (conj tempList tempMap)))
  tempList)

;;create item name list for category
(defn getItemName [nameElements]
  (def itemNameList [])
  (doseq [element nameElements]
    (def itemNameList (conj itemNameList (.html element)))
    itemNameList))

;;create item price list for category
(defn getItemPrice [priceElements]
  (def itemPriceList [])
  (doseq [element priceElements]
    (def tempStrPrice (subs (.html element) 1))
    (def intPrice (Double/parseDouble tempStrPrice))
    (def itemPriceList (conj itemPriceList intPrice))
    itemPriceList))

;;create keywords for items in category
(defn makeItemKeywords [itemList detail]
  (def tempMap {})
  (def itemMap [])
  (doseq [element itemList]
    (def tempMap (assoc tempMap (keyword detail) element))
    (def itemMap (conj itemMap tempMap)))
  itemMap)

;;create map of items in category
(defn makeItemMap [itemNameMap itemPriceMap]
  (def menuMap {})
  (def menuMap (zipmap itemNameMap itemPriceMap))
  (def menuMap (map list (keys menuMap) (vals menuMap)))

  (def finalMenu [])
  (doseq [item menuMap]
    (def tempItem (merge (last item) (first item)))
    (def finalMenu (conj finalMenu tempItem)))
  finalMenu)

;;Functions defined above
;;Below is main

(def httpConnection (Jsoup/connect "http://www.allmenus.com/ny/new-york/322071-xo-cafe-and-grill/menu/"))
(def document (.get httpConnection))
(def navDivTag (.getElementById document "menu"))
(def fullMenu (.children navDivTag))
(def seqFullMenu (seq fullMenu))

(def categoryFound (getCategory seqFullMenu))

(def categoryKeyMap (makeKeyword categoryFound))

(def fullMenu [])

;; creates the whole menu with categories and category items and prices
(dotimes [i 18]

  (def menuCategory (nth seqFullMenu i))
  
  (def nameElements (.getElementsByClass menuCategory "name"))
  (def nameList (getItemName nameElements))

  (def priceElements (.getElementsByClass menuCategory "price"))
  (def priceList (getItemPrice priceElements)) 

  (def itemNameMap (makeItemKeywords nameList "name"))
  (def itemPriceMap (makeItemKeywords priceList "price"))

  (def categoryItemMap (makeItemMap itemNameMap itemPriceMap))

  (def whichCategory (categoryKeyMap i))
  (def categoryKey (keys whichCategory))
  (def singleCategory (assoc-in whichCategory categoryKey categoryItemMap))

  (prn "HELLO WORLD!!!!"))

