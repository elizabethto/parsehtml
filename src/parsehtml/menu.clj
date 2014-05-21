(def catalog {:entree [ {:name "Pork Fried Rice" :price 5.00}
                        {:name "Chicken Pho" :price 6.00}
                        {:name "Kung Pao" :price 5.00}]
              :drinks [ {:name "Avacado Smoothie" :price 3.50}
                        {:name "Bubble Tea" :price 4.00}
                        {:name "Coconut Water" :price 4.00}]
              :appetizer [ {:name "Egg roll" :price 1.00}
                           {:name "Summer Roll" :price 5.00}
                           {:name "Spring roll" :price 2.00}]})
  

(def entree (:entree catalog))
(def item0 (entree 0))
(println (:name item0) (:price item0))

  


