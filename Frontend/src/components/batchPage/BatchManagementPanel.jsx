import { useQuery } from '@tanstack/react-query';
import LimsTableBrief from './LimsTableBrief';
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
        </div>
    )
}