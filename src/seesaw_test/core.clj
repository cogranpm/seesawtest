(ns seesaw-test.core
  (:gen-class)
  (:use seesaw.core)
  (:use seesaw.mig)
)


(defn refresh-action-handler [e]
  (println "handler invoked"))

(def refresh-action
  (action :name "Refresh" :key "menu R" :handler refresh-action-handler))


(defn add-behaviours
  [root]
  
(config! (select root [:.refresh]) :action refresh-action)
  root)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (native!)
  (invoke-later
   (-> (frame :title "Hello"
              :size [600 :by 600]
              :content (mig-panel :constraints ["wrap2" "[shrink 0]20px[200, grow, fill]" "[shrink 0]5px[]"]
                                  :items [ ["name:"] [(text)]
                                          ["age:"] [(text)]])
              :on-close :exit
              :menubar
              (menubar :items
                       [(menu :text "File" :items [(menu-item :class :refresh)])
                        (menu :text "Edit" :items [(menu-item :class :refresh)])]))
       add-behaviours
       show!)
   ))

