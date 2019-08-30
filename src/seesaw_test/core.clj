
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

(defn syntax []
  (str "multi line string
is here don't you know \n"
       "here is some call \n"
       "I am awaiting it"
   )
  )

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
  (listen btndocs :action (fn [e] (config! txtdocs :text (syntax))))
  (config! (select root [:.refresh]) :action refresh-action)
  (config! (select root [:.quit]) :action quit-action)
  root)



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
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

