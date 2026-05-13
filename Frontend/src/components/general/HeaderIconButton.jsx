export default function HeaderIconButton({
    icon,
    onClick,
    danger = false,
    hasBadge = false
}) {
    return (
        <button
            className={`header-icon-btn ${danger ? 'danger' : ''}`}
            onClick={onClick}
        >
            <span className="material-symbols-outlined">
                {icon}
            </span>

            {hasBadge && (
                <span className="header-icon-badge" />
            )}
        </button>
    );
}