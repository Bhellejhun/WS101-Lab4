/* -----------------------
   Configuration
   -----------------------
   baseUrl: point this to your Spring Boot backend.
   If your backend runs at http://localhost:8080 and exposes /api/todos, 
   keep as below. Change if different port or route.
*/
const baseUrl = "http://localhost:8080/api/todos";

/* Toggle fallback mock mode.
   - false: use backend (fetch calls)
   - true: use localStorage mock so UI works without backend
*/
const useMock = false;

/* -----------------------
   DOM elements
   ----------------------- */
const todoForm = document.getElementById("todoForm");
const todoInput = document.getElementById("todoInput");
const todoList = document.getElementById("todoList");
const statusEl = document.getElementById("status");
const template = document.getElementById("todoItemTemplate");

/* -----------------------
   Helper: show status message
   ----------------------- */
function showStatus(msg, ms = 2500) {
  statusEl.textContent = msg;
  if (ms > 0) {
    setTimeout(() => { if (statusEl.textContent === msg) statusEl.textContent = ""; }, ms);
  }
}

/* -----------------------
   Mock storage (fallback)
   ----------------------- */
function mockLoad() {
  const data = window.localStorage.getItem("mockTodos");
  return data ? JSON.parse(data) : [];
}
function mockSave(list) {
  window.localStorage.setItem("mockTodos", JSON.stringify(list));
}

/* -----------------------
   CRUD operations
   ----------------------- */

/* READ */
async function fetchTodos() {
  todoList.innerHTML = "";
  try {
    if (useMock) {
      const todos = mockLoad();
      renderTodos(todos);
      showStatus("Loaded from local mock");
      return;
    }

    const res = await fetch(baseUrl);
    if (!res.ok) throw new Error(Server responded ${res.status});
    const todos = await res.json();
    renderTodos(todos);
    showStatus("Loaded from backend");
  } catch (err) {
    showStatus("Cannot reach backend — check server (or enable mock mode).");
    console.error(err);
  }
}

/* CREATE */
async function createTodo(title) {
  try {
    if (useMock) {
      const list = mockLoad();
      const id = Date.now();
      list.push({ id, title });
      mockSave(list);
      renderTodos(list);
      showStatus("Saved (mock)");
      return;
    }

    const res = await fetch(baseUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title })
    });
    if (!res.ok) throw new Error(Create failed ${res.status});
    await fetchTodos();
    showStatus("Added");
  } catch (err) {
    showStatus("Create failed");
    console.error(err);
  }
}

/* UPDATE */
async function updateTodo(id, newTitle) {
  try {
    if (useMock) {
      const list = mockLoad().map(t => t.id === id ? {...t, title: newTitle } : t);
      mockSave(list);
      renderTodos(list);
      showStatus("Updated (mock)");
      return;
    }

    const res = await fetch(${baseUrl}/${id}, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title: newTitle })
    });
    if (!res.ok) throw new Error(Update failed ${res.status});
    await fetchTodos();
    showStatus("Updated");
  } catch (err) {
    showStatus("Update failed");
    console.error(err);
  }
}

/* DELETE */
async function deleteTodo(id) {
  try {
    if (useMock) {
      const list = mockLoad().filter(t => t.id !== id);
      mockSave(list);
      renderTodos(list);
      showStatus("Deleted (mock)");
      return;
    }

    const res = await fetch(${baseUrl}/${id}, { method: "DELETE" });
    if (!res.ok) throw new Error(Delete failed ${res.status});
    await fetchTodos();
    showStatus("Deleted");
  } catch (err) {
    showStatus("Delete failed");
    console.error(err);
  }
}

/* -----------------------
   UI rendering
   ----------------------- */
function renderTodos(todos) {
  todoList.innerHTML = "";

  if (!Array.isArray(todos) || todos.length === 0) {
    todoList.innerHTML = <li style="text-align:center;color:var(--muted)">No todos yet.</li>;
    return;
  }

  todos.forEach(todo => {
    const node = template.content.cloneNode(true);
    const li = node.querySelector("li");
    const text = node.querySelector(".todo-text");
    const editBtn = node.querySelector(".edit-btn");
    const deleteBtn = node.querySelector(".delete-btn");

    const id = todo.id ?? todo.todoId ?? null; // be flexible with backend shape
    text.textContent = todo.title ?? todo.text ?? "(no title)";

    editBtn.addEventListener("click", async () => {
      const newTitle = prompt("Edit your todo:", text.textContent);
      if (newTitle && newTitle.trim() !== "") {
        await updateTodo(id, newTitle.trim());
      }
    });

    deleteBtn.addEventListener("click", async () => {
      const ok = confirm("Delete this todo?");
      if (ok) await deleteTodo(id);
    });

    todoList.appendChild(li);
  });
}

/* -----------------------
   Setup: event listeners
   ----------------------- */
todoForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  const val = todoInput.value.trim();
  if (!val) return;
  await createTodo(val);
  todoInput.value = "";
});

window.addEventListener("load", fetchTodos);
