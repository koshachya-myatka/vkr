import NotificationCard from "./NotificationCard";

export default function NotificationsGrid({ data, reload }) {
    if (!data || data.length === 0) {
        return (
            <div className="page-section">
                <h4>Нет данных</h4>
            </div>
        );
    }

    return (
        <div className="users-grid">
            {
                data.map(item => (
                    <NotificationCard
                        key={item.id}
                        item={item}
                        reload={reload}
                    />
                ))
            }
        </div>
    );
}