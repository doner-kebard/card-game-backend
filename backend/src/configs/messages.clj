(ns configs.messages
  (:require [yaml.core :as yaml]))

(def ^:private config-file
  (yaml/from-file "../configs/config.yml" true))

(def out-of-turn (get-in config-file [:messages :out-of-turn]))

(def no-opp (get-in config-file [:messages :no-opp]))

(def play (get-in config-file [:messages :play]))

(def wait (get-in config-file [:messages :wait]))

(def row-limit (get-in config-file [:messages :row-limit]))

(def too-many-players (get-in config-file [:messages :too-many-players]))

(def need-target (get-in config-file [:messages :need-target]))

(def not-owned-card (get-in config-file [:messages :not-owned-card]))

(def no-row (get-in config-file [:messages :no-row]))

(def lobby-not-created (get-in config-file [:messages :no-lobby-not-created]))

(def invalid-id (get-in config-file [:messages :invalid-id]))
