import { useEffect } from 'react';
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

    useEffect(() => {
        if (isError) {
            const errorMessage = error?.response?.data?.message || null;
            if (error.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        }
    }, [isError, error, navigate]);

    if (isError) {
        return null;
    }

    return (
        <div className="flex-column gap-lg">
            <div className="card">
                <h2>Общая информация</h2>
                {isLoading
                    ? <Loader size="small" />
                    : <>
                        {(!batchData) && (<h4>Нет данных</h4>)}
                        {batchData && (
                            <div className="table-wrapper">
                                <table className="table">
                                    <thead>
                                        <tr>
                                            <th>ID партии</th>
                                            <th>Тип металла</th>
                                            <th>Поступление</th>
                                            <th>Обработка</th>
                                            <th>Начало анализов</th>
                                            <th>Окончание анализов</th>
                                            <th>Статус</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>{batchData.batchId}</td>
                                            <td>{batchData.metalTypeName}</td>
                                            <td>{new Date(batchData.startTime).toLocaleString()}</td>
                                            <td>{batchData.processingTime ? new Date(batchData.processingTime).toLocaleString() : ""}</td>
                                            <td>{batchData.analysesTime ? new Date(batchData.analysesTime).toLocaleString() : ""}</td>
                                            <td>{batchData.endTime ? new Date(batchData.endTime).toLocaleString() : ""}</td>
                                            <td><span className="badge badge-info">{batchData.statusName}</span></td></tr>
                                    </tbody>
                                </table>
                            </div>
                        )
                        }
                    </>
                }
            </div>

            <div className="card">
                <div className="flex-between">
                    <h2>Производственные данные</h2>
                    <span className="badge badge-info">
                        MES
                    </span>
                </div>
                {isLoading
                    ? <Loader size="small" />
                    :
                    <>
                        {(!mes || mes.length === 0) && (<h4>Нет данных</h4>)}
                        {mes && (
                            <div className="table-wrapper">
                                <table className="table">
                                    <thead>
                                        <tr>
                                            <th>ID произв. заказа</th>
                                            <th>ID обородувания</th>
                                            <th>ID оператора</th>
                                            <th>Масса шихты, т</th>
                                            <th>Масса продукта, т</th>
                                            <th>Выход годного, %</th>
                                            <th>Время обработки, мин</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>{mes.orderId ? mes.orderId : "—"}</td>
                                            <td>{mes.equipmentId ? mes.equipmentId : "—"}</td>
                                            <td>{mes.operatorId ? mes.operatorId : "—"}</td>
                                            <td>{mes.chargeMass ? mes.chargeMass.toFixed(2) : "—"}</td>
                                            <td>{mes.outputMass ? mes.outputMass.toFixed(2) : "—"}</td>
                                            <td>{batchData.outputYield ? batchData.outputYield.toFixed(2) : "—"}</td>
                                            <td>{mes.durationMin ? mes.durationMin : "—"}</td>                                            
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        )
                        }
                    </>
                }
            </div>
        </div>
    );
}