import { useQuery } from '@tanstack/react-query';
import { getBatch, getMesByBatch } from "../api/api";

export default function BatchDataPanel({ batchData }) {
    const { data: mes, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-mes', batchData.batchId],
        queryFn: () => getMesByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error.message}</div>;

    return (
        <div>
            <h2>Общая информация</h2>

            <table border="1">
                <tbody>
                    <tr>
                        <td>Тип металла</td>
                        <td>{batchData.metalTypeName}</td>
                    </tr>
                    <tr>
                        <td>Начало</td>
                        <td>{batchData.startTime}</td>
                    </tr>
                    <tr>
                        <td>Конец</td>
                        <td>{batchData.endTime}</td>
                    </tr>
                    <tr>
                        <td>Статус</td>
                        <td>{batchData.statusName}</td>
                    </tr>
                </tbody>
            </table>

            <h3>MES данные</h3>

            {mes && (
                <table>
                    <tbody>
                        {Object.entries(mes)
                            .map(([k, v]) => (
                                <tr key={k}>
                                    <td>{k}</td>
                                    <td>{String(v)}</td>
                                </tr>
                            ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}