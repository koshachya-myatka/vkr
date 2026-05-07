export default function LimsTableBriefItem({ analyses }) {
    return (
        <div>
            <table border="1" cellPadding="10" width="100%">
                <thead>
                    <tr>
                        <th>ID пробы</th>
                        <th>Метод</th>
                        <th>Дата анализа</th>
                        <th>Статус</th>
                    </tr>
                </thead>
                <tbody>
                    {analyses &&
                        (analyses?.map((analysis, index) => (
                            <tr key={index}>
                                <td>{analysis.sampleId}</td>
                                <td>{analysis.analysisMethod}</td>
                                <td>{analysis.testDate}</td>
                                <td>{analysis.statusName}</td>
                            </tr>
                        )))}
                </tbody>
            </table>
        </div>
    );
}