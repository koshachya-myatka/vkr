import { Link } from "react-router-dom";

export default function LinkButton({ text, link, active }) {
  return (
    <Link to={link}>
      <button style={{ background: active ? "#4CAF50" : "#ccc", width: "100px", height: "100px" }}>
        {text}
      </button>
    </Link>
  );
}