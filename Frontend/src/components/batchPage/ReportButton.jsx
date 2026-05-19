import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ErrorPage from '../../pages/ErrorPage';
import Loader from '../general/Loader';
import { createReport } from '../../api/api';

export default function ReportButton() {
    const navigate = useNavigate();
    const { id: batchId } = useParams();
    const [loading, setLoading] = useState(false);

    const handleClick = async () => {
        if (!batchId) return;
        setLoading(true);
        try {
            const response = await createReport(batchId);
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            window.open(url, '_blank');
            setTimeout(() => window.URL.revokeObjectURL(url), 100);
        } catch (error) {
            const errorMessage = error?.response?.data?.message || "Не удалось сформировать отчёт";
            if (error.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="btn-report">
            {loading
                ? <Loader size='supersmall' />
                :
                <button
                    onClick={handleClick}
                    disabled={loading}
                    className="btn btn-primary btn-report"
                >
                    СОЗДАТЬ ОТЧЁТ
                </button>
            }
        </div>
    );
}