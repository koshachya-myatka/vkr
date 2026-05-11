import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import LimsTableItem from './LimsTableItem';
import Loader from '../general/Loader';
import { getLimsByBatch } from "../../api/api";

export default function BatchLaboratoryPanel({ batchData }) {
    const navigate = useNavigate();

    const { data: analyses, isLoading, isError, error } = useQuery({
        queryKey: ['batch-page-lab-lims', batchData.batchId],
        queryFn: () => getLimsByBatch(batchData.batchId).then(res => res.data),
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
            <div className="flex-between">
                <h2>Лабораторные анализы</h2>
                <span className="badge badge-info">
                    LIMS
                </span>
            </div>
            {(!analyses || analyses.length === 0) && (<h4>Нет данных</h4>)}
            {analyses?.map((analysis, index) => (
                <LimsTableItem
                    key={index}
                    analysis={analysis}
                    index={index}
                />
            ))}
        </div>
    );
}