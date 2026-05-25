export default function MetalStatisticsCard({
    metalType,
    metalTypeName,
    batchesCount,
    averageOutputYield,
    defectivePercent
}) {
    const defectiveClass =
        defectivePercent > 15
            ? 'badge-danger'
            : defectivePercent > 5
                ? 'badge-warning'
                : 'badge-success';

    return (
        <div className="card card-hover info-card">
            <div className="flex-between">
                <div>
                    <h3 style={{ fontSize: "20px", marginBottom: 0 }}>
                        {metalTypeName}
                    </h3>
                </div>
                <span className={`badge ${defectiveClass}`}>
                    𒊹
                </span>
            </div>

            <div className="divider" />

            <div className="stat-block">
                <p>Данные за последнюю неделю:</p>
                <span className="stat-label">
                    Обработано партий
                </span>
                <span className="stat-value">
                    {batchesCount}
                </span>
            </div>

            <div className="flex-between">
                <small>
                    Средний выход годного
                </small>
                <span className="badge badge-success">
                    {averageOutputYield.toFixed(2)}%
                </span>
            </div>

            <div className="flex-between">
                <small>
                    Процент брака
                </small>
                <span className={`badge ${defectiveClass}`}>
                    {defectivePercent.toFixed(2)}%
                </span>
            </div>
        </div>
    );
}