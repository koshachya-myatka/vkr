import { Link } from "react-router-dom";

export default function HeaderLinkButton({ text, link, active }) {
  return (
    <Link to={link} className={`header-tab ${active ? 'active' : ''}`}>
      {text}
    </Link>
  );
}