export default function NotificationItem({ item, onTake }) {
    const severityClass = {
        INFO: 'notification-info',
        WARNING: 'notification-warning',
        ALARM: 'notification-danger'
    }[item.severity];

    return (
        <div className={`notification ${severityClass}`}>
            <div className="flex-between gap-md">
                <div className="flex-column">
                    <div className="flex-between">
                        <h4>{item.signalSource}</h4>
                        <button
                            className="btn btn-danger"
                            onClick={() => onTake(item.id)}
                        >
                            {item.severity === 'INFO' ? '✕' : 'На проверку'}
                        </button>
                    </div>
                    <small>
                        {new Date(item.createdAt).toLocaleString()}
                    </small>
                    <p>{item.message}</p>
                    <small>
                        Оборудование: {item.equipmentId}
                    </small>
                </div>
            </div>
        </div>
    );
}