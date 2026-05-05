import { useQuery } from '@tanstack/react-query';
import { getLimsWithoutResultsByBatch } from "../api/api";

export default function BatchProductionPanel({ batchData }) {
    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-prod-lims', batchData.batchId],
        queryFn: () => getLimsWithoutResultsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error.message}</div>;

    return (
        <div>
            <h2>Лабораторные анализы</h2>
            <table border="1" cellPadding="10" width="100%">
                <thead>
                    <tr>
                        <th>Sample ID</th>
                        <th>Метод</th>
                        <th>Дата</th>
                        <th>Статус</th>
                    </tr>
                </thead>
                <tbody>
                    {analyses &&
                        (analyses?.map((row, index) => (
                            <tr key={index}>
                                <td>{row.sampleId}</td>
                                <td>{row.analysisMethod}</td>
                                <td>{row.testDate}</td>
                                <td>{row.statusName}</td>
                            </tr>
                        )))}
                </tbody>
            </table>
        </div>
    );
}