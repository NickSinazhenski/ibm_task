import { useEffect, useState } from "react";
import axios from "axios";
import "./index.css";

function App() {
  const [events, setEvents] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [comments, setComments] = useState({});

  useEffect(() => {
    loadEvents();
  }, []);

  const loadEvents = async () => {
    try {
      const res = await axios.get("http://localhost:8080/events");
      if (Array.isArray(res.data)) {
        setEvents(res.data);
      } else {
        setEvents([]);
      }
    } catch {
      setEvents([]);
    }
  };

  const addEvent = async () => {
    if (!title || !description) return alert("Введите название и описание");
    await axios.post("http://localhost:8080/events", { title, description });
    setTitle("");
    setDescription("");
    loadEvents();
  };

  const handleCommentChange = (eventId, value) => {
    setComments(prev => ({ ...prev, [eventId]: value }));
  };

  const addComment = async (eventId) => {
    const text = comments[eventId];
    if (!text) return alert("Введите комментарий")
    await axios.post(`http://localhost:8080/events/${eventId}/feedback`, { text });
    setComments(prev => ({ ...prev, [eventId]: "" }));
    loadEvents();
  };

  const handleCommentKeyPress = (event, eventId) => {
    if (event.key === "Enter") {
      event.preventDefault();
      addComment(eventId);
    }
  };

  return (
    <div className="container">
      <h1>Event Feedback</h1>

      <div className="input-group">
        <input
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          className="input-field"
        />
        <input
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          className="input-field"
        />
        <button onClick={addEvent} className="btn">Add Event</button>
      </div>

      <h2>Events</h2>
      {(!Array.isArray(events) || events.length === 0) && <p>No events yet</p>}
      {Array.isArray(events) && events.map((e) => (
        <div
          key={e.id}
          className="event-card"
        >
          <h3>{e.title}</h3>
          <p>{e.description}</p>
          <small>ID: {e.id}</small>

          <div className="feedback-section">
            <h4>Feedbacks:</h4>
            {e.feedbacks && e.feedbacks.length > 0 ? (
              <ul>
                {e.feedbacks.map((fb) => {
                  let sentimentColor = "#666"; 
                  if (fb.sentiment === "POSITIVE") sentimentColor = "green";
                  else if (fb.sentiment === "NEGATIVE") sentimentColor = "red";
                  else if (fb.sentiment === "NEUTRAL") sentimentColor = "gray";
                  return (
                    <li key={fb.id || fb.timestamp}>
                      {fb.text}{" "}
                      <small style={{ color: sentimentColor, marginLeft: "5px", marginRight: "5px" }}>
                        {fb.sentiment}
                      </small>
                      <small>({new Date(fb.timestamp).toLocaleString()})</small>
                    </li>
                  );
                })}
              </ul>
            ) : (
              <p>No feedback yet</p>
            )}
          </div>

          <div className="comment-input-group">
            <input
              type="text"
              placeholder="Add a comment"
              value={comments[e.id] || ""}
              onChange={(ev) => handleCommentChange(e.id, ev.target.value)}
              onKeyDown={(ev) => handleCommentKeyPress(ev, e.id)}
              className="input-field comment-input"
            />
            <button onClick={() => addComment(e.id)} className="btn">Add Comment</button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default App;