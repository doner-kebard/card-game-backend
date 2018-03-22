"use strict";
import yaml from 'js-yaml';
import fs from 'fs';

const config = yaml.safeLoad(fs.readFileSync('/configs/config.yml', 'utf8'));
config.servers = {
    frontend: "frontend:8880",
    backend: "backend:3000",
}
module.exports = config;
