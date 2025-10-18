const readline = require("readline");
const { ScheduleManager } = require("./scheduleManager");
const { ConsoleObserver } = require("./patterns/observer");

const manager = ScheduleManager.getInstance();
const consoleObserver = new ConsoleObserver();
manager.subscribe(consoleObserver);

const rl = readline.createInterface({ input: process.stdin, output: process.stdout, prompt: "> " });

function printHelp() {
  console.log("Commands:");
  console.log(" add \"Description\" HH:MM HH:MM Priority");
  console.log(" remove \"Description\"");
  console.log(" view [Priority]");
  console.log(" edit \"Description\" [desc=New] [start=HH:MM] [end=HH:MM] [priority=Low|Medium|High]");
  console.log(" complete \"Description\"");
  console.log(" help");
  console.log(" exit");
}

function viewTasks(priority) {
  const list = manager.viewTasks(priority ? { priority } : {});
  if (list.length === 0) {
    console.log("No tasks scheduled for the day.");
    return;
  }
  list.forEach(t => {
    const done = t.completed ? "(Completed) " : "";
    console.log(`${t.startTime} - ${t.endTime}: ${t.description} [${t.priority}] ${done}`.trim());
  });
}

function parseArgs(line) {
  // Simple parser that respects quoted description
  const tokens = [];
  let current = "";
  let inQuotes = false;
  for (let i = 0; i < line.length; i++) {
    const ch = line[i];
    if (ch === '"') {
      inQuotes = !inQuotes;
      continue;
    }
    if (!inQuotes && ch === ' ') {
      if (current) {
        tokens.push(current);
        current = "";
      }
    } else {
      current += ch;
    }
  }
  if (current) tokens.push(current);
  return tokens;
}

function handleCommand(input) {
  const line = input.trim();
  if (!line) return;
  const [cmd, ...rest] = parseArgs(line);

  try {
    if (cmd === "add") {
      const [description, start, end, priority] = rest;
      manager.addTask(description, start, end, priority);
    } else if (cmd === "remove") {
      const [description] = rest;
      manager.removeTask(description);
    } else if (cmd === "view") {
      const [priority] = rest;
      viewTasks(priority);
    } else if (cmd === "edit") {
      const [description, ...kv] = rest;
      const updates = {};
      for (const pair of kv) {
        const [k, v] = pair.split("=");
        if (k === "desc") updates.description = v;
        if (k === "start") updates.startTime = v;
        if (k === "end") updates.endTime = v;
        if (k === "priority") updates.priority = v;
      }
      manager.editTask(description, updates);
    } else if (cmd === "complete") {
      const [description] = rest;
      manager.completeTask(description);
    } else if (cmd === "help") {
      printHelp();
    } else if (cmd === "exit") {
      rl.close();
      return;
    } else {
      console.log("Unknown command. Type 'help' for commands.");
    }
  } catch (err) {
    console.error(err.message || String(err));
  }
}

printHelp();
rl.prompt();
rl.on("line", (line) => {
  handleCommand(line);
  rl.prompt();
}).on("close", () => {
  console.log("Goodbye!");
  process.exit(0);
});


