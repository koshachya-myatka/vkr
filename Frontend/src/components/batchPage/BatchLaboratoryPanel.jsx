import { useQuery } from '@tanstack/react-query';
import LimsTableItem from './LimsTableItem';
import { getLimsByBatch } from "../../api/api";

export default function BatchLaboratoryPanel({ batchData }) {
    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-lab-lims', batchData.batchId],
        queryFn: () => getLimsByBatch(batchData.batchId).then(res => res.data),
        enabled: !!batchData?.batchId
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError) return <div>Ошибка: {error?.message}</div>;

    return (
        <div>
            <h2>Лабораторные анализы</h2>
            {analyses &&
                (analyses?.map((analysis, index) => (
                    <LimsTableItem analysis={analysis} index={index} />
                )))}
        </div>
    );
}