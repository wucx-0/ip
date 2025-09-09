# Ben Chatbot - Your Personal Task Manager 🐶

> "The secret of getting ahead is getting started." – Mark Twain

Ben is a simple, **text-based** task management chatbot that helps you keep track of your todos, deadlines, and events. It's designed to be *intuitive*, ~~complicated~~ **simple**, and ***incredibly*** efficient for managing your daily tasks.

## Features

Ben supports three types of tasks:
* **Todo tasks** - Simple tasks without time constraints
* **Deadline tasks** - Tasks with specific due dates
* **Event tasks** - Tasks with start and end times

## Getting Started

To use Ben, simply:
1. Clone this repository
2. Compile the Java files
3. Run the `Ben` class
4. Start adding your tasks!

## Task List Progress

- [x] Implement basic todo functionality
- [x] Add deadline support with date parsing
- [x] Create event tasks with time ranges
- [x] File-based storage system
- [ ] GUI implementation (coming soon)
- [ ] Task reminders (planned)

## Usage Examples

Here's how to add a deadline task using the `deadline` command:

```java
// Example of creating a deadline task
deadline submit assignment /by 2019-12-25
```

You can also search for tasks using the `find` keyword, mark tasks as complete with `mark`, or list all tasks with the simple `list` command.

## Technical Implementation

The chatbot follows object-oriented principles with clear separation of concerns:
* `Task` hierarchy for different task types
* `Command` pattern for user actions
* `Storage` system for data persistence
* Clean `UI` abstraction for user interaction

For more information about Java development best practices, visit the [Oracle Java Documentation](https://docs.oracle.com/en/java/).

**Ben** makes task management ***fast***, **simple**, and **effective**! 😊