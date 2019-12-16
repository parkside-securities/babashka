(ns babashka.impl.classes
  {:no-doc true}
  (:require
   [cheshire.core :as json]))

(def classes
  {:default-classes '[java.lang.ArithmeticException
                      java.lang.AssertionError
                      java.lang.Boolean
                      java.io.BufferedWriter
                      java.io.BufferedReader
                      java.lang.Class
                      java.lang.Double
                      java.lang.Exception
                      clojure.lang.ExceptionInfo
                      java.lang.Integer
                      java.io.File
                      clojure.lang.LineNumberingPushbackReader
                      java.util.regex.Pattern
                      java.lang.String
                      java.io.StringReader
                      java.io.StringWriter
                      java.lang.System
                      java.lang.Thread
                      sun.nio.fs.UnixPath
                      java.nio.file.attribute.FileAttribute
                      java.nio.file.attribute.PosixFilePermission
                      java.nio.file.attribute.PosixFilePermissions
                      java.nio.file.CopyOption
                      java.nio.file.FileAlreadyExistsException
                      java.nio.file.Files
                      java.nio.file.NoSuchFileException
                      java.nio.file.StandardCopyOption]
   :custom-classes {'java.util.concurrent.LinkedBlockingQueue ;; why?
                    {:allPublicMethods true}
                    'java.lang.Process ;; for conch?
                    {:allPublicConstructors true}}})

(defmacro gen-class-map []
  (let [classes (:default-classes classes)]
    (apply hash-map
           (for [c classes
                 c [(list 'quote c) c]]
             c))))

(def class-map (gen-class-map))

(defn generate-reflection-file
  "Generate reflection.json file"
  [& args]
  (let [entries (vec (for [c (sort (keys class-map))]
                       {:name (str c)
                        :allPublicMethods true
                        :allPublicFields true
                        :allPublicConstructors true}))]
    (spit (or
           (first args)
           "reflection2.json") (json/generate-string entries {:pretty true}))))