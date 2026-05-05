import { useQuery } from '@tanstack/react-query';
import { getLimsByBatch } from "../api/api";

export default function BatchLaboratoryPanel({ batchData }) {
    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-lab-lims', batchData.batchId],
        queryFn: () => getLimsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error.message}</div>;

    return (
        <div>
            <h2>Лабораторные анализы</h2>

            {analyses &&
                (analyses?.map((analyses, index) => (
                    <div key={index} style={{ marginBottom: "20px" }}>
                        <h3>Анализ</h3>

                        <table border="1">
                            <tbody>
                                <tr>
                                    <td>Sample ID</td>
                                    <td>{analyses.sampleId}</td>
                                </tr>
                                <tr>
                                    <td>Метод</td>
                                    <td>{analyses.analysisMethod}</td>
                                </tr>
                                <tr>
                                    <td>Дата</td>
                                    <td>{analyses.testDate}</td>
                                </tr>
                                <tr>
                                    <td>Статус</td>
                                    <td>{analyses.statusName}</td>
                                </tr>
                            </tbody>
                        </table>

                        <h4>Параметры</h4>

                        <table border="1">
                            <thead>
                                <tr>
                                    <th>Параметр</th>
                                    <th>Значение</th>
                                    <th>Ед.</th>
                                    <th>Норма</th>
                                </tr>
                            </thead>
                            <tbody>
                                {analyses.results && analyses.results?.map((p, i) => (
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
                )))}
        </div>
    );
}