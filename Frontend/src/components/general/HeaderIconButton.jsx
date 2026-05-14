export default function HeaderIconButton({
    icon,
    onClick,
    danger = false,
    hasBadge = false,
    badgeNumber = 0
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
                <div className="header-icon-badge">
                    {badgeNumber}
                </div>
            )}
        </button>
    );
}