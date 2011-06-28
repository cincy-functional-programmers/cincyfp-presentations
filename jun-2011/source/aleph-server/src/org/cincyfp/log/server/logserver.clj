(ns org.cincyfp.log.server.logserver
  (:import  (java.io.File)))

(use 'lamina.core 'aleph.tcp 'gloss.core 'gloss.io 'clojure.java.io)

(def log-writer (writer "/home/creighton/cool.log" :append true))

(def log-channel (channel))

(defn create-handler-func [output-channel] 
(fn [channel client-info]
  (enqueue channel "ok")
  (receive-all channel (fn [msg] (enqueue channel "ok")
                     (enqueue output-channel msg)))))

(defn create-logging-future [input-channel print-writer]
  (future
     (doseq [msg (lazy-channel-seq input-channel)]
       (.write print-writer (str msg))
       (.flush print-writer))))
  

(def log-handler (create-handler-func log-channel))

(def msg-frame (string :utf-8 :delimiters ["_EM_"]))
  
(start-tcp-server log-handler {:port 10000, :frame msg-frame})

(def logging-future (create-logging-future log-channel log-writer) )
