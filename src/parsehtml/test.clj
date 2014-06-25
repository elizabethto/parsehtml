(ns parsehtmltest.core)
(import 'org.jsoup.Jsoup)


(defn get-category [full-menu]
  (for [element full-menu]
    (let [find-category (.select element "h3")
          category-name (.html find-category)]
      category-name)))

(defn get-item-names [category]
  (let [item-names (.getElementsByClass category "name")
        ]
    (for [element item-names]
      (let [item-name (.html element)
            item-name-list item-name]
        item-name-list))))

(defn get-item-prices [category]
  (let [item-prices (.getElementsByClass category "price")]
    (for [element item-prices]
      (let [item-price-str (subs (.html element) 1)
            int-price (Double/parseDouble item-price-str)
            item-price-list int-price ]
        item-price-list))))

(defn make-item-name-map [item-names]
  (let [temp-map {:product/name item-names}]
    temp-map))

(defn make-item-price-map [item-prices]
  (let [temp-map {:product/price item-prices}]
    temp-map))

(defn merge-name-price [item-name-map item-price-map]
  (let [item-name item-name-map
        item-price item-price-map
        one-map (merge item-price item-name)]
    one-map))

(defn pull-menu [restaurant-url]
  (let [ http-connection (Jsoup/connect restaurant-url)
        document (.get http-connection)
        navDivTag (.getElementById document "menu")
        full-menu (.children navDivTag)]
    (seq full-menu)))

(defn parse-menu [restaurant-url]
  (let [full-menu (pull-menu restaurant-url)
        menu-category (get-category full-menu)
        num-categories (.size full-menu)
        categories (range 0 num-categories)]
    (for [i categories] 
      (let [category (nth full-menu i)
            category-item-names (get-item-names category)
            category-item-prices (get-item-prices category)
            item-name-map (map make-item-name-map category-item-names)
            item-price-map (map make-item-price-map category-item-prices)
            item-map (map merge-name-price item-name-map item-price-map)
            full-menu-map item-map]
        (println "NEW CATEGORY")
        (println item-map)
        (spit "fullmenu.txt" (pr-str item-map))
        item-map))))



