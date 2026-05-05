import { Link } from "react-router-dom";

export default function HeaderLinkButton({ text, link, active }) {
  return (
    <Link to={link} style={{ background: active ? "#4CAF50" : "#ccc", width: "100px", height: "100px" }}>
      {text}
    </Link>
  );
}