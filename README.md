# Ben Chatbot - Your Personal Task Manager 🐶

> "The secret of getting ahead is getting started." – Mark Twain

Ben is a simple, **text-based** task management chatbot that helps you keep track of your todos, deadlines, and events. It's designed to be *intuitive*, ~~complicated~~ **simple**, and ***incredibly*** efficient for managing your daily tasks.

## Features

Ben supports three types of tasks:
* **Todo tasks** - Simple tasks without time constraints
* **Deadline tasks** - Tasks with specific due dates
* **Event tasks** - Tasks with start and end times

### Advanced Features
* **Smart snoozing** - Reschedule tasks with flexible date/time options
* **Intelligent search** - Find tasks by keywords or due dates
* **Dual interface** - Choose between CLI and GUI modes
* **Auto-save** - Your tasks persist automatically between sessions

## Getting Started

### Prerequisites
- Java 17 or higher
- JavaFX (for GUI mode)

### Installation
1. Clone this repository
   ```bash
   git clone https://github.com/yourusername/ben-chatbot.git
   cd ben-chatbot
   ```

2. Compile the Java files
   ```bash
   ./gradlew build
   ```

3. Run Ben:

   **GUI:**
   ```bash
    ./gradlew run
   ```

## Usage Examples

### Adding Tasks

Here's how to add different types of tasks:

```java
// Add a simple todo task
todo Buy groceries for the week

// Add a deadline task with specific due date
deadline Submit CS2103T project /by 2024-12-15

// Add an event with start and end times
event Team meeting /from 2024-11-25 1400 /to 2024-11-25 1500
```

### Managing Tasks

```java
// List all tasks
list

// Mark task 2 as complete
mark 2

// Delete task 1
delete 1

// Find tasks containing "meeting"
find meeting

// View tasks due on a specific date
due 2024-12-01
```

### Advanced Snoozing

```java
// Postpone deadline by 3 days
snooze 1 +3

// Reschedule event to specific date/time
snooze 2 2024-12-25 1400

// Change only the start time (preserves duration)
snooze 3 /start 2024-12-01 1300

// Change event duration to 2 hours
snooze 3 /duration 120

// Set both start and end times
snooze 3 2024-12-01 1400 1600
```

## Architecture Overview

The chatbot follows object-oriented principles with clear separation of concerns:

```java
// Core class structure
public abstract class Task {
    private String description;
    private boolean isComplete;
    
    public abstract String getType();
    // ... other methods
}

public class Deadline extends Task {
    private LocalDate deadline;
    
    public Deadline(String description, String dateString) throws BenException {
        super(description);
        this.deadline = parseDate(dateString);
    }
}

// Command pattern implementation  
public abstract class Command {
    public abstract void execute(TaskList tasks, UI ui, Storage storage) 
        throws BenException;
    public abstract boolean isExit();
}
```

### Key Components

* **Task hierarchy** - `Task`, `ToDo`, `Deadline`, `Event` classes for different task types
* **Command pattern** - Modular command system for user actions (`AddCommand`, `SnoozeCommand`, etc.)
* **Storage system** - File-based persistence with automatic save/load
* **GUI** - graphical User interfaces (`GUI`)
* **Parser** - Intelligent command parsing with comprehensive error handling

## Task List Progress

- [x] Implement basic todo functionality
- [x] Add deadline support with date parsing
- [x] Create event tasks with time ranges
- [x] File-based storage system
- [x] GUI implementation with JavaFX
- [x] Advanced snoozing with flexible options
- [x] Search and filtering capabilities
- [x] Comprehensive error handling
- [x] Data persistence across sessions

## Command Reference

| Command | Format | Example |
|---------|--------|---------|
| `todo` | `todo <description>` | `todo Buy milk` |
| `deadline` | `deadline <description> /by <date>` | `deadline Assignment /by 2024-12-01` |
| `event` | `event <description> /from <datetime> /to <datetime>` | `event Meeting /from 2024-12-01 1400 /to 2024-12-01 1500` |
| `list` | `list` | `list` |
| `mark` | `mark <task_number>` | `mark 1` |
| `unmark` | `unmark <task_number>` | `unmark 2` |
| `delete` | `delete <task_number>` | `delete 3` |
| `find` | `find <keyword>` | `find project` |
| `due` | `due <date>` | `due 2024-12-01` |
| `snooze` | `snooze <task_number> <options>` | `snooze 1 +7` |
| `bye` | `bye` | `bye` |

## Date Formats

Ben uses ISO date formats for consistency:
- **Dates**: `yyyy-MM-dd` (e.g., 2024-12-25)
- **Times**: `yyyy-MM-dd HHmm` (e.g., 2024-12-25 1400)
- **Relative**: `+<days>` (e.g., +7 for one week later)

## Technical Implementation

### Storage Format
Tasks are stored in `./data/ben.txt` using a pipe-separated format:
```
T | 0 | Buy groceries
D | 1 | Submit report | 2024-12-15  
E | 0 | Team meeting | 2024-11-25T14:00 | 2024-11-25T15:00
```

### Error Handling
Ben provides comprehensive error messages for invalid inputs:
- Date format validation
- Task number bounds checking
- Command syntax verification
- File I/O error recovery

For more information about Java development best practices, visit the [Oracle Java Documentation](https://docs.oracle.com/en/java/).

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

**Ben** makes task management ***fast***, **simple**, and **effective**!