import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Loader from '../general/Loader';
import LimsTableBrief from './LimsTableBrief';
import ScadaParameterAvg from './ScadaParameterAvg';
import { getLimsWithoutResultsByBatch } from "../../api/api";
import { getScadaAvgByBatch } from '../../api/api';

export default function BatchManagementPanel({ batchData }) {
    const navigate = useNavigate();

    const { data: analyses, isLoading: isLoadingLims, isError: isErrorLims, error: errorLims } = useQuery({
        queryKey: ['batch-page-prod-lims', batchData.batchId],
        queryFn: () => getLimsWithoutResultsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    const { data: scada, isLoading: isLoadingScada, isError: isErrorScada, error: errorScada } = useQuery({
        queryKey: ['batch-page-manag-scada', batchData.batchId],
        queryFn: () => getScadaAvgByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    useEffect(() => {
        if (isErrorLims) {
            const errorMessage = errorLims?.response?.data?.message || null;
            if (errorLims.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        }
    }, [isErrorLims, errorLims, navigate]);

    useEffect(() => {
        if (isErrorScada) {
            const errorMessage = errorScada?.response?.data?.message || null;
            if (errorScada.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        }
    }, [isErrorScada, errorLims, navigate]);

    if (isErrorLims || isErrorScada) {
        return null;
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
                {isLoadingLims
                    ? <Loader size="small" />
                    : <LimsTableBrief analyses={analyses} />}
            </div>

            <div className="card">
                <div className="flex-between">
                    <h2>Показатели оборудования</h2>
                    <span className="badge badge-info">
                        SCADA
                    </span>
                </div>
                <div className="divider" />
                {isLoadingScada
                    ? <Loader size="small" />
                    : <>
                        {scada && scada.length > 0 ? (
                            <div className="scada-grid">
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
                            <h4>
                                Нет данных
                            </h4>
                        )}
                    </>
                }
            </div>
        </div>
    )
}