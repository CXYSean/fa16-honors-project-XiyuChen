(ns adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))

(def the-map
  {

    :foyer {:desc "The walls are freshly painted but do not have any pictures.  You get the feeling it was just created
for a game or something."
           :title "in the foyer. There is a stair to go up."
           :dir {:south :grue-pen
                 :north :garden
                 :west :bedroom
                 :east :gallery
                 :upstair :livingroom}
           :contents #{:raw-egg}}
   :grue-pen {:desc "It is very dark.  You are about to be eaten by a grue."
              :title "in the grue pen"
              :dir {:north :foyer
                    :west :bathroom
                    :east :gameroom}
              :contents #{}}
   :garden {:desc "it is a garden."
             :title "in the garden"
             :dir {:south :foyer}
             :contents #{:gate}}
   :bedroom {:desc "it is a bedroom for guests."
             :title "in the bedroom."
             :dir {:east :foyer
                   :sorth :bathroom}
             :contents #{}}
   :gallery {:desc "it is a gallery."
             :title "in the gallery."
             :dir {:west :foyer
                   :south :gameroom}
             :contents #{}}
   :bathroom {:desc "You are in the bathroom. There is still blood on the wall."
              :title "in the bathroom."
              :dir {:north :bedroom
                    :east :grue-pen}
              :contents #{}}
   :gameroom {:desc "You are in the gameroom. There is a snooker table. There is Stair to go down"
              :title "in the gameroom."
              :dir {:north :gallery
                    :west :grue-pen
                    :downstair :basement}
              :contents #{:balls}}
   :backyard {:desc "You are in the backyard. There are so many grass. You cannot see what is under the grass."
              :title "in the backyard."
              :dir {:south :grue-pen}
              :contents #{}}
   :studyroom {:desc ""
               :title "in the studyroom."
               :dir {:west :livingroom}
               :contents #{:books}}
   :livingroom {:desc "You are in the livingroom. There is a painting on the wall."
                :title "in the living room."
                :dir {:downstair :foyer
                      :upstair :attic
                      :east :studyroom
                      :west :kitchen}
                :contents #{:torch}}
   :attic {:desc "It's an attic and it is very dark, you cannot see anything"
           :title "in the attic."
           :dir {:downstair :livingroom}
           :contents #{:map}}
    :basement {:desc "It is the basement, it is very dark, you cannot see anything without the torch."
               :title "in the basement."
               :dir {:upstair :gameroom}
               :contents #{:box}}
    :kitchen{:desc "You are in the kitchen. There is a pot to cook."
             :title "in the kitchen."
             :dir {:east :livingroom}
             :contents #{:pot}}
    :forest{:desc "You are in the forest."
            :title "in the forest."
            :dir {:south :garden}
            :contents #{}}


    :raw-egg{:desc "It may be an egg from chicken"
             :title "an egg"}
    :torch{:ignite true
           :desc "it's a torch. It brings light"}
    :box{:open 1
         :desc "You need to solve the question to open the box"
         :contents #{:key}}
    :balls{:desc "it is just normal balls"}
    :pot{:desc "You can use it to cook egg"}
    :key{:desc "You can go outside wtih the key."
         :title "a room key"}
    :gate{:desc "You cannot open it without the key"}
    :fried-egg{:desc "a fried egg"
               :title "fried egg"}
    :books{:desc "You need to get the key to get out of that house. It is in a very dark place."
           :title "books"}
    :map{:desc "There is a key in the basement. But which room leads to the basement. You need to try."
         :title "map for the house"}

})

(def adventurer
  {:location :foyer
   :inventory #{}
   :tick 0
   :seen #{}})

(defn status [player]
  (let [location (player :location)]
    (print (str "You are " (-> the-map location :title) ". "))
    (when-not ((player :seen) location)
      (print (-> the-map location :desc)))
    (update-in player [:seen] #(conj % location))))

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn go [dir player]
  (let [location (player :location)
        dest (->> the-map location :dir dir)]
    (if (nil? dest)
      (do (println "You can't go that way.")
          player)
      (assoc-in player [:location] dest))))

(defn search [contents player]
  (let [location (player :location)
        basement (-> the-map :basement)
        items (->> the-map location :contents)]
    (if (items :box)
      (if (player :inventory :torch)
        (do (println items)
            player)
        (do (println "Find the torch. You cannot see anything right now.")
            player))
      (if (empty? items)
        (do (println "There is nothing.")
            player)
        (do (println items)
            player)))))

(defn pick [thing player]
  (let [location (player :location)
        things (->> the-map location :contents)]
    (if (things thing)
      (update-in player [:inventory] #(conj % thing))
      (do (println "it's not there. You cannot pick it up")
          player))))

(defn show [items player]
  (let [things (player :inventory)]
    (if (empty? things)
      (do (println "You have nothing.")
          player)
      (do (println things)
          player))))


(defn unlock [item player]
  (let [location (player :location)
        roomkey (-> the-map :key)
        things (->> the-map location :contents)]
    (if (things :box)
      (let [_ (println "How long is it from s to e?")
            command (read-line)]
        (if (= command "mile")
          (update-in player [:inventory] #(conj % :key))
          (do (println "You  got the wrong answer.")
            player)))
      (do (println "The box is not here.")
          player))))

(defn cook [food player]
  (let [location (player :location)
        mything (player :inventory)
        things (->> the-map location :contents)]
    (if (mything :raw-egg)
      (if (things :pot)
        (update-in player [:inventory] #(conj % :fried-egg))
        (do (println "You need to find the pot.")
            player))
      (do (println "You don't have anything to cook") player))))

(defn out [roomkey player]
  (let [loction (player :location)
        items (player :inventory)]
    (if (loction :contents :gate)
      (if (items roomkey)
        (do (assoc-in player [:location] :forest) (println "You are out of the house.") player)
        (do (println "You need to find the key.")
            player))
      (do (println "You cannot go out the house from here.") player))))

(defn watch [books player]
  (let [location (player :location)
        things (->> the-map location :contents)
        items (player :inventory)]
    (if (or (things books) (items books))
      (do (println "The "(-> the-map books :title) " tell you: " (-> the-map books :desc))
          player)
      (do (println "You don't have anything to read. Find the books or map and take them with you")
          player))))

(defn eat [food player]
  (let [things (player :inventory)]
    (if (things food)
      (do (println "You eat the fried egg. You are powerful again.") player)
      (do (println "You don't have anything to eat.")
          player))))

(defn tock [player]
  (update-in player [:tick] inc))

(defn respond [player command]
  (match command
         [:look] (update-in player [:seen] #(disj % (-> player :location)))
         (:or [:n] [:north] ) (go :north player)
         [:south] (go :south player)
         [:west] (go :west player)
         [:east] (go :east player)
         [:upstair] (go :upstair player)
         [:downstair] (go :downstair player)
         [:search] (search :contents player)
         [:inventory](show :inventory player)
         [:egg] (pick :raw-egg player)
         [:torch] (pick :torch player)
         [:balls] (pick :balls player)
         [:books] (pick :books player)
         [:unlock] (unlock :box player)
         [:cook] (cook :raw-egg player)
         [:out] (out :key player)
         [:read] (watch :books player)
         [:map] (watch :map player)
         [:eat] (eat :fried-egg player)


         _ (do (println "I don't understand you.")
               player)
         ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [local-map the-map
         local-player adventurer]
    (let [pl (status local-player)
          _  (println "What do you want to do?")
          command (read-line)]
      (if (= command "exit")
        (println "That's the end")
        (recur local-map (respond pl (to-keywords command)))))))
