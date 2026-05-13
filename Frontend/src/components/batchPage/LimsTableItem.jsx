export default function LimsTableItem({ analysis, index }) {
    return (
        <div className="card">
            <div className="flex-between">
                <div>
                    <h3 style={{ fontSize: "20px", marginBottom: 0 }}>
                        Анализ #{index + 1}
                    </h3>
                </div>
                <span className={"badge badge-" + (analysis.status === 'APPROVED' ? "success" : "danger")}>
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
                            <td>{analysis.analysisMethod}</td>
                        </tr>
                        <tr>
                            <td>Дата анализа</td>
                            <td>{new Date(analysis.testDate).toLocaleString()}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div className="divider" />
            <h4>Параметры анализа</h4>
            {analysis.results && analysis.results.length > 0
                ? (<div className="table-wrapper">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Параметр</th>
                                <th>Значение</th>
                                <th>Ед. изм.</th>
                                <th>Соотв. норме</th>
                            </tr>
                        </thead>
                        <tbody>
                            {analysis.results?.map((p, i) => (
                                <tr key={i}>
                                    <td>{p.parameterName}</td>
                                    <td>{!isNaN(Number(p.value)) ? Number(p.value).toFixed(3) : p.value}</td>
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
                </div>)
                : <p>Нет данных</p>
            }
        </div>
    );
}