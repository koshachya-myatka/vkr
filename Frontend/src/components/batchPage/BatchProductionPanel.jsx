import { useQuery } from '@tanstack/react-query';
import LimsTableBrief from './LimsTableBrief';
import ScadaParameterGraph from './ScadaParameterGraph';
import { getLimsWithoutResultsByBatch } from "../../api/api";
import { getScadaByBatch } from '../../api/api';

export default function BatchProductionPanel({ batchData }) {
    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-prod-lims', batchData.batchId],
        queryFn: () => getLimsWithoutResultsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    const { data: scada = [] } = useQuery({
        queryKey: ['batch-page-prod-scada', batchData.batchId],
        queryFn: () => getScadaByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error?.message}</div>;

    return (
        <div className="flex-column gap-lg">
            <div className="card">
                <div className="flex-between">
                    <h2>
                        Лабораторные анализы
                    </h2>
                    <span className="badge badge-info">
                        LIMS
                    </span>
                </div>
                <div className="divider" />
                <LimsTableBrief analyses={analyses} />
            </div>

            <div className="card">
                <div className="flex-between">
                    <h2>SCADA данные</h2>
                    <span className="badge badge-success">
                        Онлайн мониторинг
                    </span>
                </div>

                <div className="divider" />
                {scada.length > 0 ? (
                    <div className="flex-column gap-lg">
                        {scada.map((parameter) => (
                            <div
                                key={`${parameter.equipmentId}_${parameter.parameter}`}
                                className="card card-hover"
                            >
                                <ScadaParameterGraph
                                    parameter={parameter}
                                />
                            </div>
                        ))}

                    </div>
                ) : (
                    <div className="card">
                        Нет SCADA данных
                    </div>
                )}
            </div>
        </div>
    );
}