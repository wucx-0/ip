# Ben Chatbot User Guide

Ben is a task management chatbot that helps you organize your daily activities through a simple command-line or graphical interface. Whether you need to track simple todos, manage deadlines, or schedule events, Ben provides an intuitive way to stay organized.

## Quick Start

### Running Ben

**Command Line Interface (CLI):**
```bash
java ben.Ben
```

**Graphical User Interface (GUI):**
```bash
java ben.Ben gui
```

## Features

### Adding Tasks

Ben supports three types of tasks:

#### Todo Tasks
Simple tasks without specific dates or times.

**Format:** `todo <description>`

**Example:**
```
todo Eat dinner
todo Call dentist
```

#### Deadline Tasks
Tasks with specific due dates.

**Format:** `deadline <description> /by <yyyy-MM-dd>`

**Example:**
```
deadline Submit project report /by 2024-12-15
deadline Pay electricity bill /by 2024-11-30
```

#### Event Tasks
Tasks with specific start and end times.

**Format:** `event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>`

**Example:**
```
event Team meeting /from 2024-11-25 1400 /to 2024-11-25 1500
event Doctor appointment /from 2024-12-01 0900 /to 2024-12-01 1030
```

### Managing Tasks

#### Viewing All Tasks
Display all your tasks with their completion status.

**Command:** `list`

**Sample Output:**
```
Here are the tasks in your list:
1.[T][ ] Buy groceries
2.[D][ ] Submit project report (deadline: Dec 15 2024)
3.[E][X] Team meeting (startTime: Nov 25 2024 1400 endTime: Nov 25 2024 1500)
```

#### Marking Tasks as Complete
Mark tasks as done when you finish them.

**Format:** `mark <task number>`

**Example:**
```
mark 1
mark 3
```

#### Unmarking Tasks
Mark previously completed tasks as incomplete.

**Format:** `unmark <task number>`

**Example:**
```
unmark 2
```

#### Deleting Tasks
Remove tasks from your list permanently.

**Format:** `delete <task number>`

**Example:**
```
delete 1
```

### Finding Tasks

#### Search by Keyword
Find tasks containing specific words in their descriptions.

**Format:** `find <keyword>`

**Example:**
```
find meeting
find project
```

#### View Tasks Due on Specific Date
See all deadline tasks due on a particular date.

**Format:** `due <yyyy-MM-dd>`

**Example:**
```
due 2024-12-01
```

### Snoozing Tasks

Ben's advanced snooze feature allows you to reschedule deadline and event tasks with flexible options.

#### Basic Snoozing

**Postpone by Days:**
```
snooze 1 +3          # Postpone task 1 by 3 days
snooze 2 +7          # Postpone task 2 by 1 week
```

**Reschedule to Specific Date:**
```
snooze 1 2024-12-25          # Reschedule deadline to Christmas Day
snooze 2 2024-11-30          # Reschedule deadline to November 30
```

#### Advanced Event Snoozing

**Change Event Start Time (preserve duration):**
```
snooze 3 /start 2024-12-01 1300     # Change start time only
snooze 3 /start 2024-12-01          # Change date, keep time
```

**Change Event Duration (keep start time):**
```
snooze 3 /duration 60        # Make event 1 hour long
snooze 3 /duration 120       # Make event 2 hours long
```

**Set Both Start and End Times:**
```
snooze 3 2024-12-01 1400 1600       # Event from 2-4 PM on Dec 1
```

**Full Control with /from and /to:**
```
snooze 3 /from 2024-12-01 1400 /to 2024-12-01 1630
```

**Note:** Only deadline and event tasks can be snoozed. Todo tasks don't have specific dates to reschedule.

### Exiting Ben

To close Ben and save your tasks:

**Command:** `bye`

## Date and Time Formats

Ben uses specific formats for dates and times:

- **Dates:** `yyyy-MM-dd` (e.g., 2024-12-25)
- **Times:** `yyyy-MM-dd HHmm` (e.g., 2024-12-25 1400 for 2:00 PM)
- **Relative dates:** `+<number>` (e.g., +3 for 3 days later)

## Data Storage

Ben automatically saves your tasks to a file located at `./data/ben.txt`. Your tasks persist between sessions, so you can close Ben and resume your work later without losing any data.

The same task data is shared between CLI and GUI modes, so you can switch between interfaces as needed.

## Interface Options

### Command Line Interface (CLI)
- Text-based interaction
- Fast command entry
- Full feature support
- Ideal for keyboard users

### Graphical User Interface (GUI)
- Visual chat-like interface
- Resizable window
- Mouse and keyboard support
- User-friendly for beginners

Both interfaces provide identical functionality, so choose the one that best fits your workflow.

## Tips for Effective Use

1. **Use descriptive task names** to easily identify tasks later
2. **Set realistic deadlines** to maintain productivity
3. **Use the find command** to quickly locate specific tasks
4. **Take advantage of snoozing** to adapt to changing schedules
5. **Regular use of list command** helps you stay on top of your tasks
6. **Mark tasks as complete** for a sense of accomplishment

## Troubleshooting

### Common Issues

**"Invalid date format" errors:**
- Ensure dates follow the `yyyy-MM-dd` format
- Check that times use 24-hour format (`HHmm`)

**"Task number must be a valid number" errors:**
- Use the number shown in the `list` command
- Ensure the task number exists in your current list

**Tasks not appearing:**
- Use the `list` command to refresh your view
- Check if tasks were accidentally deleted

**Snooze not working:**
- Verify you're trying to snooze a deadline or event task
- Check that your date/time format is correct

For any persistent issues, restart Ben to reload your task data from storage.

---
