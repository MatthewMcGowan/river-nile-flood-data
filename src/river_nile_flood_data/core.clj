(ns river-nile-flood-data.core
  (:gen-class))

(require '[clojure.string :as str])

(def filename "resources/nile.csv")

(defn parseLines
  "Takes the contents of a file, returns a list of lines."
  [fileContents]
  (str/split fileContents #"\n"))

(defn parseLine
  "Takes a single csv line, returning a list of column values."
  [line]
  (str/split line #","))

(defn parseHeader
  "Takes a header line, and returns as a list of keywords."
  [headerLine]
  (map keyword (parseLine headerLine)))

(defn mapifyRecord
  "Takes a list of column headers and a list of values, and returns a map of values."
  [keywords record]
  (zipmap keywords record))

(defn parseFile
  "Takes the contents of csv with header, returning a list of maps."
  [fileContents]
  (let [lines (parseLines fileContents)
        header (parseHeader (first lines))
        records (map parseLine (rest lines))
        mapify (partial mapifyRecord header)]
    (map mapify records)))

(defn str->int
  [str]
  (Integer. str))

(defn str->dec
  [str]
  (BigDecimal. str))

;; Flood is actually a number
(defn parseNileRecords
  "Takes a list of maps, and parses the Flood height to a number."
  [records]
  (map
    #(update % :Year str->int)
    (map #(update % :Flood str->dec) records)))

(defn highestYears
  "Takes a list of maps of nile flood data, returning the year(s) with the highest flood."
  [records]
  (let [maxFlood (apply max (map :Flood records))]
    (filter #(= maxFlood (:Flood %)) records)))

;(highestYears (parseNileRecords (parseFile (slurp filename))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (doseq
    [year (highestYears (parseNileRecords (parseFile (slurp filename))))]
    (println year)))
