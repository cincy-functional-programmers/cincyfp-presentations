(ns org.cincyfp.log4j.appender.core
  (:import  (org.apache.log4j.AppenderSkeleton)
            (org.apache.log4j.spi.LoggingEvent)
            (org.apache.log4j.spi.ErrorCode)
            (org.apache.log4j.Layout)
            (org.apache.log4j.helpers.LogLog))
  (:gen-class
    :name "org.cincyfp.log4j.appender.CljAppender"
    :extends org.apache.log4j.AppenderSkeleton
    :state        state
    :init         myinit
    :methods [[getHost [] String] 
              [setHost [String] void]
							[getPort [] int] 
              [setPort [int] void]
              [createAsyncHandler [] void]
              [getHandlerFunc [] Runnable]]))
  
(use 'lamina.core 'aleph.tcp 'gloss.core)


(defn -myinit [] 
  (let [ch (channel)]
  [[] (ref {:name "clj-append" :channel ch})]))
  

(defn -activateOptions [this]
   (let [state (.state this)
         host (.getHost this)
         port (.getPort this)
         frame (string :utf-8 :delimiters ["_EM_"])
         tcp  (tcp-client {:host host, :port port, :frame frame})]
     (dosync (alter state assoc :tcp-client tcp))
     (.createAsyncHandler this)))


(defn -append [this #^LoggingEvent event]
  (let [message (.format (.getLayout this) event)]
    (enqueue (:channel @(.state this)) message)))


(defn -requiresLayout [this] true)

(defn -close [this] )


(defn -getHost [this] 
  (:host @(.state this)))

(defn -setHost [this nm]
  (let [state (.state this)]
        (dosync (alter state assoc :host nm)) nil))

(defn -getPort [this] 
  (:port @(.state this)))

(defn -setPort [this port]
  (let [state (.state this)]
        (dosync (alter state assoc :port port)) nil))


(defn- -createAsyncHandler [this]
  (doto 
    (Thread. (.getHandlerFunc this))
    (.setDaemon true)
    (.start)))


(defn- -getHandlerFunc [this] 
  (let [ch (:channel @(.state this))
        tcp (:tcp-client @(.state this))]
    (fn [] (doseq [msg (lazy-channel-seq ch)]
             (enqueue @tcp msg)))))


(defn -main [& args]
  (let [tmp (new org.cincyfp.log4j.appender.CljAppender)]
    (println "Host1:" (.getHost tmp))
    (.setHost tmp "localhost")
 		(println "Host2:" (.getHost tmp))
    (.setPort tmp 20000)
    (println "Port:" (.getPort tmp))))
