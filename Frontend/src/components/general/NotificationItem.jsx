export default function NotificationItem({ item, onDelete }) {
    const severityClass = {
        WARNING: 'notification-warning',
        ALARM: 'notification-danger'
    }[item.severity];

    return (
        <div className={`notification ${severityClass}`}>
            <div className="flex-between gap-md">
                <div className="flex-column gap-sm">
                    <h4>{item.signalSource}</h4>
                    <small>
                        {new Date(item.createdAt).toLocaleString()}
                    </small>
                    <p>{item.message}</p>
                    <small>
                        Оборудование: {item.equipmentId}
                    </small>
                </div>
                <button
                    className="btn btn-danger"
                    onClick={() => onDelete(item.id)}
                >
                    ✕
                </button>
            </div>
        </div>
    );
}