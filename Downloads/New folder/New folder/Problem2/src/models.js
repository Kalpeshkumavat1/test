// Domain models and time utilities for the Astronaut Daily Schedule Organizer

const VALID_PRIORITIES = ["Low", "Medium", "High"];

function parseTimeToMinutes(hhmm) {
  if (typeof hhmm !== "string") return null;
  const match = hhmm.match(/^([01]\d|2[0-3]):([0-5]\d)$/);
  if (!match) return null;
  const hours = Number(match[1]);
  const minutes = Number(match[2]);
  return hours * 60 + minutes;
}

function minutesToTimeString(totalMinutes) {
  const hours = Math.floor(totalMinutes / 60);
  const minutes = totalMinutes % 60;
  const hh = String(hours).padStart(2, "0");
  const mm = String(minutes).padStart(2, "0");
  return `${hh}:${mm}`;
}

class Task {
  constructor({ description, startTime, endTime, priority }) {
    if (!description || typeof description !== "string" || !description.trim()) {
      throw new Error("Error: Description is required.");
    }

    const start = parseTimeToMinutes(startTime);
    const end = parseTimeToMinutes(endTime);
    if (start === null || end === null) {
      throw new Error("Error: Invalid time format.");
    }
    if (end <= start) {
      throw new Error("Error: End time must be after start time.");
    }

    if (!VALID_PRIORITIES.includes(priority)) {
      throw new Error("Error: Invalid priority. Use Low, Medium, or High.");
    }

    this.description = description.trim();
    this.startTime = minutesToTimeString(start);
    this.endTime = minutesToTimeString(end);
    this.startMinutes = start;
    this.endMinutes = end;
    this.priority = priority;
    this.completed = false;
  }

  overlaps(otherTask) {
    return this.startMinutes < otherTask.endMinutes && otherTask.startMinutes < this.endMinutes;
  }
}

module.exports = {
  Task,
  VALID_PRIORITIES,
  parseTimeToMinutes,
  minutesToTimeString,
};


