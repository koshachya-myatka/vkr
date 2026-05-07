import { useQuery } from '@tanstack/react-query';
import LimsTableBriefItem from './LimsTableBriefItem';
import { getLimsWithoutResultsByBatch } from "../../api/api";

export default function BatchManagementPanel({ batchData }) {
    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-prod-lims', batchData.batchId],
        queryFn: () => getLimsWithoutResultsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error?.message}</div>;

    return (
        <div>
            <h2>Лабораторные анализы</h2>
            <LimsTableBriefItem analyses={analyses} />

            
        </div>
    )
}