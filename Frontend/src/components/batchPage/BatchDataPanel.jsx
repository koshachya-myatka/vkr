import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Loader from '../general/Loader';
import { getBatch, getMesByBatch } from "../../api/api";

export default function BatchDataPanel({ batchData }) {
    const navigate = useNavigate();

    const { data: mes, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-mes', batchData.batchId],
        queryFn: () => getMesByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) {
        return (
            <Loader size="small" />
        );
    }

    if (isError) {
        navigate("/error");
    }

    if (!batchData) {
        <div className="page-section">
            <h4>Нет данных</h4>
        </div>
    }

    return (
        <div className="flex-column gap-lg">
            <div className="card">
                <h2>Общая информация</h2>
                <div className="table-wrapper">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>ID партии</th>
                                <th>Тип металла</th>
                                <th>Поступление</th>
                                <th>Окончание анализов</th>
                                <th>Статус</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{batchData.batchId}</td>
                                <td>{batchData.metalTypeName}</td>
                                <td>{batchData.startTime}</td>
                                <td>{batchData.endTime}</td>
                                <td><span className="badge badge-info">{batchData.statusName}</span></td></tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="card">
                <div className="flex-between">
                    <h2>MES данные</h2>
                    <span className="badge badge-info">
                        Производство
                    </span>
                </div>
                {(!mes) && (<h4>Нет данных</h4>)}
                {mes && (
                    <div className="table-wrapper">
                        <table className="table">
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
                                    <td><span className="badge badge-success">{mes.statusName}</span></td></tr>
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}