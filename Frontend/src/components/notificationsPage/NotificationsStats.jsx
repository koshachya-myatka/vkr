export default function NotificationsStats({ stats }) {
    return (
        <div className="grid-cards">
            <div className="card card-hover info-card">
                <div className="stat-block">
                    <span className="stat-label">
                        Всего уведомлений
                    </span>
                    <span className="stat-value badge badge-info">
                        {stats.totalToday ?? 0}
                    </span>
                </div>
            </div>

            <div className="card card-hover info-card">
                <div className="stat-block">
                    <span className="stat-label">Создано</span>
                    <span className="stat-value badge badge-danger">
                        {stats.createdCount ?? 0}
                    </span>
                </div>
            </div>

            <div className="card card-hover info-card">
                <div className="stat-block">
                    <span className="stat-label">В работе</span>
                    <span className="stat-value badge badge-warning">
                        {stats.inProgressCount ?? 0}
                    </span>
                </div>
            </div>

            <div className="card card-hover info-card">
                <div className="stat-block">
                    <span className="stat-label">Ложное срабатывание</span>
                    <span className="stat-value badge badge-info">
                        {stats.falsePositiveCount ?? 0}
                    </span>
                </div>
            </div>

            <div className="card card-hover info-card">
                <div className="stat-block">
                    <span className="stat-label">Причина устранена</span>
                    <span className="stat-value badge badge-success">
                        {stats.resolvedCount ?? 0}
                    </span>
                </div>
            </div>
        </div>
    );
}