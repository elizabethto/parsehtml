(ns parsehtmltest.core)
(import 'org.jsoup.Jsoup)


(defn extract-category-name [element]
  (let [h3 (.select element "h3")
        category-name (.html h3)]
    category-name))

(defn get-category [menu-seq]
  (map extract-category-name menu-seq))


(defn get-item-names [category]
  (let [item-names (.getElementsByClass category "name")
        item-name-list []]
    (for [element item-names]
      (let [item-name (.html element)
            item-name-list (conj item-name-list item-name)]
        item-name-list))))

(defn pull-menu [restaurant-url]
  (let [ http-connection (Jsoup/connect restaurant-url)
        document (.get http-connection)
        navDivTag (.getElementById document "menu")
        full-menu (.children navDivTag)]
    (seq full-menu)))

(defn parse-menu [restaurant-url]
  (let [full-menu (pull-menu restaurant-url)
        menu-category (get-category full-menu)]
    (println menu-category)
    (dotimes [i (.size full-menu)]
      (let [category (nth full-menu i)
            category-item-names (get-item-names category)
            ]
        (println category-item-names)
        ))))


