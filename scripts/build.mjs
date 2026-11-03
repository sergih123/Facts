// Builds www/ for the Android app from the same index.html the web version uses.
import { mkdirSync, readFileSync, writeFileSync, copyFileSync, existsSync } from 'node:fs';

const version = process.env.FACTS_VERSION_NAME || 'dev';
mkdirSync('www', { recursive: true });

let html = readFileSync('index.html', 'utf8');
html = html.replaceAll('__APP_VERSION__', version);
// Capacitor's JS runtime (registerPlugin etc.) must load before the app script.
html = html.replace('<script>\n"use strict";', '<script src="capacitor.js"></script>\n<script>\n"use strict";');
if (!html.includes('src="capacitor.js"')) throw new Error('Could not inject capacitor.js');
writeFileSync('www/index.html', html);

const core = 'node_modules/@capacitor/core/dist/capacitor.js';
if (!existsSync(core)) throw new Error('Run npm install first (missing ' + core + ')');
copyFileSync(core, 'www/capacitor.js');
console.log('www/ built, version', version);
