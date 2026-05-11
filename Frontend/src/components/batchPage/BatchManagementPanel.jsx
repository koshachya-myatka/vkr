import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Loader from '../general/Loader';
import LimsTableBrief from './LimsTableBrief';
import ScadaParameterAvg from './ScadaParameterAvg';
import { getLimsWithoutResultsByBatch } from "../../api/api";
import { getScadaAvgByBatch } from '../../api/api';

export default function BatchManagementPanel({ batchData }) {
    const navigate = useNavigate();

    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-prod-lims', batchData.batchId],
        queryFn: () => getLimsWithoutResultsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    const { data: scada = [] } = useQuery({
        queryKey: ['batch-page-manag-scada', batchData.batchId],
        queryFn: () => getScadaAvgByBatch(batchData.batchId).then(res => res.data),
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
                {scada && scada.length > 0 ? (
                    <div className="flex-column gap-lg">
                        {scada.map((parameter) => (
                            <div
                                key={`${parameter.equipmentId}_${parameter.parameter}`}
                                className="card card-hover"
                            >
                                <ScadaParameterAvg
                                    parameter={parameter}
                                />
                            </div>
                        ))}

                    </div>
                ) : (
                    <div className="card">
                        Нет данных
                    </div>
                )}
            </div>
        </div>
    )
}