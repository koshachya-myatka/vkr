export default function LimsTableItem({
    analysis,
    index
}) {
    return (
        <div className="card">
            <div className="flex-between">
                <div>
                    <h3 className="card-title">
                        Анализ #{index + 1}
                    </h3>
                    <small>
                        {analysis.analysisMethod}
                    </small>
                </div>
                <span className="badge badge-info">
                    {analysis.statusName}
                </span>

            </div>
            <div className="divider" />
            <div className="table-wrapper">
                <table className="table">
                    <tbody>
                        <tr>
                            <td>ID пробы</td>
                            <td>{analysis.sampleId}</td>
                        </tr>
                        <tr>
                            <td>Метод</td>
                            <td>
                                {analysis.analysisMethod}
                            </td>
                        </tr>
                        <tr>
                            <td>Дата анализа</td>
                            <td>{analysis.testDate}</td>
                        </tr>
                        <tr>
                            <td>Статус</td>
                            <td>
                                <span className="badge badge-info">
                                    {analysis.statusName}
                                </span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div className="divider" />
            <h3>Параметры анализа</h3>
            <div className="table-wrapper">
                <table className="table">
                    <thead>
                        <tr>
                            <th>Параметр</th>
                            <th>Значение</th>
                            <th>Ед. изм.</th>
                            <th>Норма</th>
                        </tr>
                    </thead>
                    <tbody>
                        {analysis.results?.map((p, i) => (
                            <tr key={i}>
                                <td>{p.parameterName}</td>
                                <td>{p.value}</td>
                                <td>{p.unit}</td>
                                <td>
                                    {p.normal ? (
                                        <span className="badge badge-success">
                                            ✔
                                        </span>
                                    ) : (
                                        <span className="badge badge-danger">
                                            ✖
                                        </span>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}