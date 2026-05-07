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
        <div>
            <h2>Лабораторные анализы</h2>
            <LimsTableBrief analyses={analyses} />

            <h2>Данные SCADA</h2>
            {scada &&
                (scada.map((parameter) => (
                    <ScadaParameterGraph parameter={parameter} />
                )))}
        </div>
    );
}