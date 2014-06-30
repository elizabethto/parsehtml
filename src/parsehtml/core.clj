(ns parsehtml.core)
(import 'org.jsoup.Jsoup)


(defn get-db-format []
  (let [db-map-format {:db/id  "#db/id[:db.part/user]"}]
    db-map-format))

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
        db-format (get-db-format)
        one-map (merge item-price item-name db-format)]
    one-map))

(defn connect-url [restaurant-url]
  (let [http-connection (Jsoup/connect restaurant-url)
        website (.get http-connection)]
    website))

(defn pull-menu [restaurant-url]
  (let [website (connect-url restaurant-url)
        menu-tag (.getElementById website "menu")
        full-menu (.children menu-tag)]
    (seq full-menu)))

(defn get-address-map [info]
  (let [address-line1 (.text (.get info 1))
        address-line1-map {:address/line1  address-line1}
        city (.text (.get info 2))
        city-map {:address/city city}
        state (.text (.get info 3))
        state-map {:address/state state}
        zipcode (.text (.get info 4))
        zipcode-map {:address/zipcode zipcode}
        db-format (get-db-format)
        address-map (merge zipcode-map state-map city-map address-line1-map db-format)]
    address-map))

(defn get-store-info [restaurant-url]
  (let [website (connect-url restaurant-url)
        store-name-tag (.select website "h1")
        store-name (.text store-name-tag)
        store-name-map {:store/name store-name}
        info-tag (.getElementById website "primary_info")
        info (.select info-tag "[itemprop]")
        address-map (get-address-map info)
        final-address-map {:address address-map} 
        phone (.text (.last info))
        phone-map {:phone phone}
        all-info (merge store-name-map final-address-map phone-map)]
    all-info))

(defn get-menu-info [restaurant-url]
  (let [full-menu (pull-menu restaurant-url)
        menu-category (get-category full-menu)
        num-categories (.size full-menu)
        categories (range 0 2)]
    (for [i categories] 
      (let [category (nth full-menu i)
            category-name-map {:category/name (nth menu-category i)}
            category-item-names (get-item-names category)
            category-item-prices (get-item-prices category)
            item-name-map (map make-item-name-map category-item-names)
            item-price-map (map make-item-price-map category-item-prices)
            item-map (map merge-name-price item-name-map item-price-map)
            vector-map (into [] item-map)
            category-product-map {:category/products vector-map}
            db-format (get-db-format)
            all-menu (merge category-product-map category-name-map db-format)]
       
        all-menu))))


(defn store-edn-file [restaurant-url]
  (let [store-info-map (get-store-info restaurant-url)
        catolog-info (get-menu-info restaurant-url)
        catolog-info-map (into [] catolog-info)
        catolog-map {:catalog catolog-info-map}
        db-format (get-db-format)
        edn-file-map (merge catolog-map store-info-map db-format)
        abc (pr-str edn-file-map)
        edn-temp-one (clojure.string/replace abc #"\"#" "#")
        edn-temp-two (clojure.string/replace edn-temp-one #"user]\"" "user]") 
        edn-x (clojure.string/replace edn-temp-two #"," " ")
        edn-final (str \[ edn-x \])]
    (spit "finalmenu.txt" edn-final)
    edn-final))



