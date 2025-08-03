
# Workforce Management - Starter Project


## My Approach to Solving the Challenge

This section outlines my thought process and technical approach for fixing the bugs and implementing the new features.

### Bug Fixes

#### Bug 1: Task Re-assignment Creates Duplicates
* **The Goal:** The core problem was that re-assigning work to a new employee left the old task active, causing confusing duplicates.
* **My Approach:** I identified that the existing logic was correctly finding all open tasks for a reference, but it was reassigning all of them instead of just one. My plan was to modify this logic to: 1) Isolate one task to be assigned, and 2) Explicitly cancel any other open duplicates.
* **Implementation:** I updated the `assignByReference` method. The code now gets the list of applicable open tasks, assigns the **first task** in the list to the new user, and then iterates through the **rest of the list** to set their status to `CANCELLED`.
* **Result:** This fix ensures that only one active task remains for any given assignment, providing a clean data state and preventing user confusion.

#### Bug 2: Cancelled Tasks Clutter the View
* **The Goal:** The user's task list was cluttered with `CANCELLED` tasks, which are no longer relevant to their daily work.
* **My Approach:** I diagnosed this as a simple filtering issue. The API endpoint responsible for fetching tasks was not distinguishing between active and cancelled tasks.
* **Implementation:** I applied a straightforward fix in the `fetchTasksByDate` method. I added a `.filter()` condition to the Java Stream that processes the tasks, ensuring any task where `task.getStatus() == TaskStatus.CANCELLED` is excluded from the final list.
* **Result:** The API now returns a cleaner response containing only actionable tasks, improving the user experience.

### New Features

#### Feature 1: "Smart" Daily Task View
* **The Goal:** The user needed a more useful "daily workload" view that showed not just tasks created today, but also older, overdue tasks that were still active.
* **My Approach:** I determined that a simple date range query was insufficient. The logic needed to be based on a task's **creation date** and its **current status**. My plan was to first augment the data model to capture the creation time.
* **Implementation:**
    1.  I added a `creationTime` field to the `TaskManagement` model.
    2.  I updated the `createTasks` method to populate this field with a timestamp upon creation.
    3.  I rewrote the `fetchTasksByDate` filter logic. It now returns any task that is **not** `COMPLETED` or `CANCELLED` and has a `creationTime` that is **on or before** the query's `end_date`. This single condition elegantly includes both new tasks and all older, still-open tasks.
* **Result:** The endpoint now provides a complete and accurate picture of an employee's entire workload, which is significantly more useful for daily operations.

#### Feature 2: Implement Task Priority
* **The Goal:** Managers needed the ability to set, change, and find tasks based on a priority level (`HIGH`, `MEDIUM`, `LOW`).
* **My Approach:** I broke the problem down into two distinct functionalities: updating a single task's priority and fetching a list of tasks by a specific priority. This led to a plan of creating two new, dedicated endpoints.
* **Implementation:**
    1.  **For updating:** I created a new `PUT` endpoint at `/{id}/priority`. This endpoint takes the new priority in the request body, updates the task's field, and saves it.
    2.  **For fetching:** I added a `findByPriority` method to the `TaskRepository` and exposed it through the service layer with a new `GET` endpoint at `/priority/{priority}`.
* **Result:** This feature provides managers with crucial control over their team's focus, allowing them to highlight critical tasks and easily review all high-priority items at once.

#### Feature 3: Implement Task Comments & Activity History
* **The Goal:** The team needed both a full audit trail for each task (history) and a way for users to collaborate via comments.
* **My Approach:** Instead of building two separate systems, I designed a single, flexible solution. My plan was to create a unified `TaskActivity` model that could represent either a system-generated event or a user-added comment, distinguished by a `type` enum.
* **Implementation:**
    1.  I created the `TaskActivity` model and added a `List<TaskActivity>` to the main `TaskManagement` model.
    2.  I modified the existing service methods (`create`, `update`, etc.) to automatically call a helper method (`addHistoryLog`) that adds `HISTORY` type activities when key events occur.
    3.  I created a new `POST` endpoint at `/{id}/comments` to allow users to add their own `COMMENT` type activities.
    4.  Finally, I ensured the `findTaskById` method sorts all activities chronologically before returning them.
* **Result:** The application now has a robust history system. When a user fetches a single task, they get a complete, sorted timeline of every event and conversation, which greatly improves transparency and collaboration.
