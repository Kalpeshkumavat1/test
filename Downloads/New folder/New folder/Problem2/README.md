# Astronaut Daily Schedule Organizer (Console App)

A Node.js console application to manage daily astronaut tasks with time windows, priorities, conflict detection, and basic CRUD. Implements Singleton, Factory, and Observer patterns.

## Run

1. Install Node.js 18+.
2. In the project directory, run:

```
npm start
```

If no start script exists yet, use:

```
node src/index.js
```

## Commands

- `add "Description" HH:MM HH:MM Priority`
- `remove "Description"`
- `view [Priority]`
- `edit "Description" [desc=New] [start=HH:MM] [end=HH:MM] [priority=Low|Medium|High]`
- `complete "Description"`
- `help`
- `exit`

## Examples

```
add "Morning Exercise" 07:00 08:00 High
add "Team Meeting" 09:00 10:00 Medium
view
remove "Morning Exercise"
add "Lunch Break" 12:00 13:00 Low
add "Training Session" 09:30 10:30 High   # -> conflict with Team Meeting
```

## Design Patterns

- Singleton: `ScheduleManager.getInstance()`
- Factory: `TaskFactory.createTask(...)`
- Observer: `Subject` + `ConsoleObserver` for notifications


