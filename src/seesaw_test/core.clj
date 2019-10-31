
(ns seesaw-test.core
  (:gen-class)
  (:use seesaw.core)
  (:use seesaw.mig)
  )

(defn make-button
  [caption]
  (button :text caption)
  )

(defn make-text-multi
  []
  (text :multi-line? true :font "MONOSPACED-PLAIN-14"))


                                        ;tutorial stuff
(def txtdocs (make-text-multi))
(def btndocs (make-button "run"))


(defn writein [line]
  (text! txtdocs (str (text txtdocs) line "\n" ":---------\n\n" )))



(defn syntax []
  (writein "hello there")
  (writein (str
   "if then else is a bit different
(if boolean-form then-form optional-else form \n"
       "(if true 
         something
         something else) \n"
       (if true
         "zeus"
         "aquaman")
       "\n\n"
       "multiline if use the do operator \n"
       "(if true
(do something1
something2)
(do somethingelse1
somethingelse2)"
       "when operator: combo if and do with no else branch
(when true 
  something1
  something2)\n\n"

       
       ))

  (writein "check for nil use nil?")
  (writein "(nil? someval)")

  ;trying out some let and def bindings within a function
  (let [myname "paul"]
    (writein myname))
  (def lemare "lemare")
  (writein lemare)

                                        ;equality
  (if (= 1 1) (writein "1 = 1"))


  )

(defn docs []
  (syntax))

(defn refresh-action-handler [e]
  (println "handler invoked"))

(defn quit-action-handler [e]
  (println "quit")
  (System/exit 0))


(def refresh-action
  (action :name "Refresh" :key "menu R" :handler refresh-action-handler))

(def quit-action
  (action :name "Quit" :key "menu Q" :handler quit-action-handler))


(defn make-items
  []
  [
   ["name:"] [(text)]
   ["age:"] [(text)]
   ["button:"] [btndocs]
   ["docs:"] [txtdocs]
   ] )


(defn add-behaviours
  [root]
  (listen btndocs :action (fn [e] (docs)))
  (config! (select root [:.refresh]) :action refresh-action)
  (config! (select root [:.quit]) :action quit-action)
  root)



;;note the use of mig panel
;;you provide the layout for each column of each row in a string
;;
;;
;;type of grid:
;;["wrap2"
;;row1:
;;"[shrink 0]20px[200, grow, fill]"
;;row2:
;;"[shrink 0]5px[]5px[]20px[grow, fill]"]

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (native!)
  (invoke-later
   (-> (frame :title "Hello"
              :size [600 :by 600]
              :content (mig-panel
                        :constraints ["wrap2" "[shrink 0]20px[200, grow, fill]" "[shrink 0]5px[]5px[]20px[grow, fill]"]
                        :items (make-items)
                        )
              :on-close :exit
              :menubar
              (menubar :items
                       [(menu :text "File" :items [(menu-item :class :refresh) (menu-item :class :quit)])
                        (menu :text "Edit" :items [(menu-item :class :quit)])]))
       add-behaviours
       show!)
   ))

