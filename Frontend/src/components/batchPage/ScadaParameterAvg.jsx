export default function ScadaParameterAvg({ parameter }) {

    return (
        <div className="flex-column gap-md">
            <div className="flex-between">
                <div className="flex-column">
                    <h3>
                        {parameter.parameter}, {parameter.unit}
                    </h3>
                    <span className="text-secondary">
                        {parameter.equipmentId}
                    </span>
                </div>
            </div>

            <div className="divider" />

            <div className="grid-3">
                <div className="card">
                    <div className="text-secondary">
                        Ср. знач.
                    </div>
                    <div className="metric-value">
                        {parameter.avgValue}
                    </div>
                </div>

                <div className="card">
                    <div className="text-secondary">
                        Минимум
                    </div>
                    <div className="metric-value">
                        {parameter.minValue}
                    </div>
                </div>

                <div className="card">
                    <div className="text-secondary">
                        Максимум
                    </div>
                    <div className="metric-value">
                        {parameter.maxValue}
                    </div>
                </div>
            </div>

            <div className="text-secondary">
                Количество измерений:
                {' '}
                {parameter.valuesCount}
            </div>
        </div>
    );
}