(defproject log4j-appender "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [aleph "0.2.0-alpha1"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]]
  :dev-dependencies[
        [lein-eclipse "1.0.0"]
        [swank-clojure "1.2.1"]]
  :main org.cincyfp.log4j.appender.core)
