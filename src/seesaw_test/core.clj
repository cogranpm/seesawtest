;; notes on repl style dev ie sending buffer to cider and trying stuff out
;; frame is defined as a var in main_frame
;; examples
;; https://gist.github.com/daveray/1441520
;; put content in the frame f
;; (config! f :content lbl)
;; (config! txtDocs :background :pink :foreground "#00f")
;; a function to display some content
;; (defn display [content]
;;(config! f :content content)
;; content)


;; (config! widget :property value)   to change stuff
;; (config widget :property) to get value of stuff
;; frame has a :content property you can use to set the guts of item
;; create a label -> (def name (label "some text"))

;; set up some colors - can use css color names and codes
;; set multiple properties in a single config! call
;; (config! widget :background :pink :foreground "#00f")

;; change font
;; (config! widget :font "ARIAL-BOLD-21")
;; or use (font :name :monospaced
;;        :style #{:bold :italic}
;;        :size 18)

;; button
;;(def b (button :text "Click me"))
;; event handler most of the time e arg is the widget that triggered event
;; the action does not have to return a function it can just do some stuff
;; (listen widget :action (fn [e] (some action)))
;; can do multiple handlers at once
;; note that % parameter is the event argument, which is usually the widget
;; (listen widget :mouse-entered #(config! % :foreground :blue)
;;                :mouse-exited #(config! % :foreground :red))


;; list boxes - :model takes any sequence (finite), displays using str
;; (def lb (listbox :model (-> 'seesaw.core ns-publics keys sort)))
;; to make it scroll
;; (scrollable lb)

;; selections
;; (selection lb)
;; returns nil if no selection
;; otherwise you get the item from the model
;; multiple selection 
;; (selection lb {:multi? true})
;;set the selection:
;;(selection! widget 'value)
;; selection change event:
;; (listen widget :selection (fn [e] (println "seleciton is " (selection e))))


;;Text boxes
;; (text "this is the text")
;; query the text
;;(text widget)
;; change the text
;; (text! widget "new value")
;; multi line
;; (text :multi-line? true :text "some text")
;; you can auto insert from certain "readable" objects
;; (text! widget (java.net.URL. "http://clojure.com"))
;; put it in a scrollable
;; use (scroll! widget :to [:line 50])  or could be :top or :bottom

;; Splitter
;; (split (left-right-split (scrollable widget-1) (scrollable widget-2) :divider-location 1/3))
;;
;; Border panel
;; (border-panel
;;   :north (horizontal-panel :items widgets))
;;   :center split
;;   :vgap 5 :hgap 5 :border 5))







(ns seesaw-test.core
  (:gen-class)
  (:use seesaw.core)
  (:use seesaw.dev)
  (:use seesaw.mig)
  )



(defn make-button
  [caption]
  (button :text caption)
  )

(defn make-text-multi
  []
  (text :multi-line? true :font "MONOSPACED-PLAIN-14"))

(defn make-combo
  []
  (combobox  :model {1 "one" 2 "two" 3 "three"}))
  ;;(combobox :model ["one" "two" "three"]))
                                        ;tutorial stuff
(def txtdocs (make-text-multi))
(def btndocs (make-button "run"))

;;combo box with data
(def cboTest (make-combo))


(defn writein [line]
  (text! txtdocs (str (text txtdocs) line "\n" ":---------\n\n" )))


;;just to put some stuff in the multiline text box
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
   ["combo"] [cboTest]
   ["button:"] [btndocs]
   ["docs:"] [txtdocs]
   ] )


(defn add-behaviours
  [root]
  (listen btndocs :action (fn [e] (docs)))
  (config! (select root [:.refresh]) :action refresh-action)
  (config! (select root [:.quit]) :action quit-action)
  root)

(defn make-frame []
  (native!)
  (frame :title "Hello"
         :size [600 :by 600]
         :content (mig-panel
                   :constraints ["wrap2" "[shrink 0]20px[200, grow, fill]" "[shrink 0]5px[]5px[]20px[grow, fill]"]
                   :items (make-items)
                   )
         ;;get rid of the following or the repl will die when window is closed
         ;;:on-close :exit
         :menubar
         (menubar :items
                  [(menu :text "File" :items [(menu-item :class :refresh) (menu-item :class :quit)])
                   (menu :text "Edit" :items [(menu-item :class :quit)])]))

  )


(def main-frame
  (make-frame)
    )

;; can call this to change what displays in the frame
(defn display [content]
  (config! main-frame :content content)
  content)


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
   (-> main-frame 
       add-behaviours
       show!)
   ))

