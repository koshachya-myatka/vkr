export default function LimsTableBrief({ analyses }) {
    if (!analyses || analyses.length === 0) {
        return (
            <div className="card">
                Анализы отсутствуют
            </div>
        );
    }

    return (
        <div className="table-wrapper">
            <table className="table">
                <thead>
                    <tr>
                        <th>ID пробы</th>
                        <th>Метод</th>
                        <th>Дата анализа</th>
                        <th>Статус</th>
                    </tr>
                </thead>
                <tbody>
                    {analyses && analyses.map((analysis, index) => (
                        <tr key={index}>
                            <td>{analysis.sampleId}</td>
                            <td>{analysis.analysisMethod}</td>
                            <td>{analysis.testDate}</td>
                            <td>
                                <span className="badge badge-info">
                                    {analysis.statusName}
                                </span>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}