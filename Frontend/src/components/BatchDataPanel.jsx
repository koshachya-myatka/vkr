import { useQuery } from '@tanstack/react-query';
import { getBatch, getMesByBatch } from "../api/api";

export default function BatchDataPanel({ batchData }) {
    const { data: mes, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-mes', batchData.batchId],
        queryFn: () => getMesByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error?.message}</div>;

    return (
        <div>
            <h2>Общая информация</h2>

            <table border="1">
                <thead>
                    <tr>
                        <th>Тип металла</th>
                        <th>Поступление</th>
                        <th>Окончание анализов</th>
                        <th>Статус</th>
                    </tr>
                </thead>
                <tbody>
                    <tr><td>{batchData.metalTypeName}</td>
                        <td>{batchData.startTime}</td>
                        <td>{batchData.endTime}</td>
                        <td>{batchData.statusName}</td></tr>
                </tbody>
            </table>

            <h3>MES данные</h3>

            {mes && (
                <table border="1">
                    <thead>
                        <tr><th>ID обородувания</th>
                            <th>ID оператора</th>
                            <th>Температура</th>
                            <th>Давление</th>
                            <th>Время обработки</th>
                            <th>Энергопотребление</th>
                            <th>Статус</th></tr>
                    </thead>
                    <tbody>
                        <tr><td>{mes.equipmentId}</td>
                            <td>{mes.operatorId}</td>
                            <td>{mes.temperature}</td>
                            <td>{mes.pressure}</td>
                            <td>{mes.durationSec}</td>
                            <td>{mes.energyConsumption}</td>
                            <td>{mes.statusName}</td></tr>
                    </tbody>
                </table>
            )}
        </div>
    );
}