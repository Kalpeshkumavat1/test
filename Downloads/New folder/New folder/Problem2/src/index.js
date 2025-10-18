// Entrypoint: setup logging and launch CLI
const fs = require("fs");
const path = require("path");

const logFile = path.join(__dirname, "..", "logs", `app.log`);
function log(message) {
  const line = `[${new Date().toISOString()}] ${message}\n`;
  try {
    fs.appendFileSync(logFile, line);
  } catch (_) {
    // ignore logging failures
  }
}

process.on("uncaughtException", (err) => {
  log(`uncaughtException: ${err.stack || err.message}`);
});
process.on("unhandledRejection", (reason) => {
  log(`unhandledRejection: ${reason && reason.stack ? reason.stack : reason}`);
});

log("Application starting");
require("./cli");



