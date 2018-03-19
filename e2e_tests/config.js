"use strict";
import yaml from 'js-yaml';
import fs from 'fs';

const config = yaml.safeLoad(fs.readFileSync('/configs/config.yml', 'utf8'));

module.exports = config;
