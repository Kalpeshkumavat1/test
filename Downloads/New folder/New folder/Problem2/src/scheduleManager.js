// ScheduleManager singleton implementing CRUD, sorting, and conflict detection
const fs = require("fs");
const path = require("path");
const { TaskFactory } = require("./factory");
const { Subject } = require("./patterns/observer");
const { VALID_PRIORITIES } = require("./models");

class ScheduleManager extends Subject {
  constructor() {
    super();
    if (ScheduleManager._instance) {
      return ScheduleManager._instance;
    }
    this.taskFactory = new TaskFactory();
    this.tasks = [];
    ScheduleManager._instance = this;
  }

  static getInstance() {
    return new ScheduleManager();
  }

  addTask(description, startTime, endTime, priority) {
    const task = this.taskFactory.createTask(description, startTime, endTime, priority);
    const conflict = this.tasks.find(t => t.overlaps(task));
    if (conflict) {
      this.notify({ type: "conflict", with: conflict, task });
      throw new Error(`Error: Task conflicts with existing task "${conflict.description}".`);
    }
    this.tasks.push(task);
    this.tasks.sort((a, b) => a.startMinutes - b.startMinutes);
    this.notify({ type: "added", task });
    return task;
  }

  removeTask(description) {
    const idx = this.tasks.findIndex(t => t.description === description);
    if (idx === -1) {
      throw new Error("Error: Task not found.");
    }
    const [removed] = this.tasks.splice(idx, 1);
    this.notify({ type: "removed", task: removed });
    return removed;
  }

  editTask(description, updates) {
    const task = this.tasks.find(t => t.description === description);
    if (!task) throw new Error("Error: Task not found.");

    const newDesc = updates.description !== undefined ? String(updates.description).trim() : task.description;
    const newStart = updates.startTime !== undefined ? updates.startTime : task.startTime;
    const newEnd = updates.endTime !== undefined ? updates.endTime : task.endTime;
    const newPriority = updates.priority !== undefined ? updates.priority : task.priority;

    // Create a temp task to validate
    const temp = this.taskFactory.createTask(newDesc, newStart, newEnd, newPriority);
    const conflict = this.tasks.find(t => t.description !== description && t.overlaps(temp));
    if (conflict) {
      this.notify({ type: "conflict", with: conflict, task: temp });
      throw new Error(`Error: Task conflicts with existing task "${conflict.description}".`);
    }

    task.description = temp.description;
    task.startTime = temp.startTime;
    task.endTime = temp.endTime;
    task.startMinutes = temp.startMinutes;
    task.endMinutes = temp.endMinutes;
    task.priority = temp.priority;

    this.tasks.sort((a, b) => a.startMinutes - b.startMinutes);
    this.notify({ type: "updated", task });
    return task;
  }

  completeTask(description) {
    const task = this.tasks.find(t => t.description === description);
    if (!task) throw new Error("Error: Task not found.");
    task.completed = true;
    this.notify({ type: "completed", task });
    return task;
  }

  viewTasks(filter = {}) {
    const { priority } = filter;
    if (priority && !VALID_PRIORITIES.includes(priority)) {
      throw new Error("Error: Invalid priority. Use Low, Medium, or High.");
    }
    let list = this.tasks;
    if (priority) list = list.filter(t => t.priority === priority);
    if (list.length === 0) return [];
    return list.slice().sort((a, b) => a.startMinutes - b.startMinutes);
  }
}

module.exports = { ScheduleManager };


