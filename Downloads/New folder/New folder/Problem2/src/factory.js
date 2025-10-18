// Factory for creating Task instances
const { Task } = require("./models");

class TaskFactory {
  createTask(description, startTime, endTime, priority) {
    return new Task({ description, startTime, endTime, priority });
  }
}

module.exports = { TaskFactory };


