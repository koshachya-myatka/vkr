export default function LimsTableItem({ analysis, index }) {
    return (
        <div key={index} style={{ marginBottom: "20px" }}>
            <h3>Анализ</h3>

            <table border="1">
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
                        <td>{analysis.testDate}</td>
                    </tr>
                    <tr>
                        <td>Статус</td>
                        <td>{analysis.statusName}</td>
                    </tr>
                </tbody>
            </table>

            <h4>Параметры</h4>

            <table border="1">
                <thead>
                    <tr>
                        <th>Параметр</th>
                        <th>Значение</th>
                        <th>Ед. изм.</th>
                        <th>Норма</th>
                    </tr>
                </thead>
                <tbody>
                    {analysis.results && analysis.results?.map((p, i) => (
                        <tr key={i}>
                            <td>{p.parameterName}</td>
                            <td>{p.value}</td>
                            <td>{p.unit}</td>
                            <td>{p.normal ? "✔" : "✘"}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}