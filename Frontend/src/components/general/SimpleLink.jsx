import { Link } from "react-router-dom";

export default function SimpleLink({ className, text, link, style }) {
  return (
    <Link to={link} style={style} className={className}>
      {text}
    </Link>
  );
}