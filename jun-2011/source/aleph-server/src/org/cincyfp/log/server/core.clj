(ns org.cincyfp.log.server.core
  (:import  (java.io.File)))

(use 'lamina.core 'aleph.tcp 'gloss.core 'gloss.io 'clojure.java.io)

(def log-writer (writer "/home/creighton/cool.log" :append true))

(def log-channel (channel))

(defn log-handler [channel client-info]
  (enqueue channel "ok")
  (receive-all channel (fn [msg] (enqueue channel "ok")
                     (enqueue log-channel msg))))

(def f (future
     (doseq [msg (lazy-channel-seq log-channel)]
       (.write log-writer (str msg))
       (.flush log-writer))))
  
(def msg-frame (string :utf-8 :delimiters ["_EM_"]))
  
(start-tcp-server log-handler {:port 10000, :frame msg-frame})
