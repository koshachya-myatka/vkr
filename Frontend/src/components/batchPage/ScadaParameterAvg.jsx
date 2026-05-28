export default function ScadaParameterAvg({ parameter }) {
    const metrics = [
        {
            label: "Ср. значение",
            value: parameter.avgValue
        },
        {
            label: "Минимум",
            value: parameter.minValue
        },
        {
            label: "Максимум",
            value: parameter.maxValue
        },
        {
            label: "Кол-во измерений",
            value: parameter.valuesCount
        }        
    ];

    return (
        <div className="scada-parameter">
            <div className="scada-header">
                <div className="flex-column">
                    <h3>
                        {parameter.parameter}, {parameter.unit}
                    </h3>
                </div>
                <span className="badge badge-info">
                    {parameter.equipmentId}
                </span>
            </div>
            <div className="divider" />
            <div className="scada-metrics">
                {metrics.map(metric => (
                    <div key={metric.label} className="metric-card">
                        <div className="metric-label">
                            {metric.label}
                        </div>
                        <div className="metric-value">
                            {metric.label === "Кол-во измерений"
                                ? metric.value
                                : (metric.value ?? 0).toFixed(2)
                            }
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}