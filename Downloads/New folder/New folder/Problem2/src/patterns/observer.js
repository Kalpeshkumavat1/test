// Simple Observer pattern primitives for notifications

class Subject {
  constructor() {
    this.observers = new Set();
  }

  subscribe(observer) {
    this.observers.add(observer);
    return () => this.observers.delete(observer);
  }

  notify(event) {
    for (const observer of this.observers) {
      try {
        observer.update(event);
      } catch (_) {
        // Ignore observer errors to avoid breaking flow
      }
    }
  }
}

class ConsoleObserver {
  update(event) {
    if (event.type === "conflict") {
      console.error(`Error: Task conflicts with existing task "${event.with.description}".`);
    } else if (event.type === "added") {
      console.log("Task added successfully. No conflicts.");
    } else if (event.type === "removed") {
      console.log("Task removed successfully.");
    } else if (event.type === "updated") {
      console.log("Task updated successfully.");
    } else if (event.type === "completed") {
      console.log("Task marked as completed.");
    }
  }
}

module.exports = {
  Subject,
  ConsoleObserver,
};


