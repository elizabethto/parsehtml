(ns parsehtml.core)
(import 'org.jsoup.Jsoup)


(defn getCategory [seqFullMenu]
  (def categoryList [])

  (doseq [element seqFullMenu]
    (def findCategory (.select element "h3"))
    (def categoryName (.html findCategory))
    (def categoryList (conj categoryList categoryName)))
  categoryList)

(defn makeKeyword [listToChange]
  (def tempMap {})
  (def tempList [])

  (doseq [element listToChange]
    (def tempMap {(keyword element) nil})
    (def tempList (conj tempList tempMap)))
  tempList)

(defn getItemName [nameElements]
  (def itemNameList [])
  (doseq [element nameElements]
    (def temp (.html element))
    (def itemNameList (conj itemNameList temp)))
  itemNameList)

(defn getItemPrice [priceElements]
  (def itemPriceList [])
  (doseq [element priceElements]
    (def tempStrPrice (subs (.html element) 1))
    (def intPrice (Double/parseDouble tempStrPrice))
    (def itemPriceList (conj itemPriceList intPrice)))
  itemPriceList)

(defn makeItemKeywords [itemList detail]
  (def tempMap {})
  (def itemMap [])
  (doseq [element itemList]
    (def tempMap (assoc tempMap (keyword detail) element))
    (def itemMap (conj itemMap tempMap)))
  itemMap)

(defn makeItemMap [itemNameMap itemPriceMap]
  (def menuMap {})
  (def menuMap (zipmap itemNameMap itemPriceMap))
  (def menuMap (map list (keys menuMap) (vals menuMap)))

  (def finalMenu [])
  (doseq [item menuMap]
    (def tempItem (merge (last item) (first item)))
    (def finalMenu (conj finalMenu tempItem)))
  finalMenu)



{defn getRestaurantMenu [restaurantURL]

 (def httpConnection (Jsoup/connect restaurantURL))

 (def document (.get httpConnection))
 (def navDivTag (.getElementById document "menu"))
 (def fullMenu (.children navDivTag))
 (def seqFullMenu (seq fullMenu))

 (def categoryFound (getCategory seqFullMenu))

 (def categoryKeyMap (makeKeyword categoryFound))

 (def fullMenu {})

 (dotimes [i (.size seqFullMenu)]

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

  
   (def fullMenu (merge fullMenu singleCategory))
   (spit "fullmenu.txt" (apply str fullMenu)))

 fullMenu}


{defn parser-menu [restaurantURL]

 (def menu (getRestaurantMenu restaurantURL))
 menu
 }

