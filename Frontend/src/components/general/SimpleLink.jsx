import { Link } from "react-router-dom";

export default function SimpleLink({ text, link, style }) {
  return (
    <Link to={link} style={style}>
      {text}
    </Link>
  );
}