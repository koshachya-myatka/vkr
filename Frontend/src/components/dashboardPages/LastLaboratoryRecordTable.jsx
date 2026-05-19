import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Loader from '../general/Loader';
import { getLastLims } from "../../api/api";

export default function LastLaboratoryRecordTable() {
    const navigate = useNavigate();

    const { data: data, isLoading, isError, error } = useQuery({
        queryKey: ['dashboard-lab-last-lims'],
        queryFn: () => getLastLims().then(res => res.data)
    });

    useEffect(() => {
        if (isError) {
            const errorMessage = error?.response?.data?.message || null;
            if (error.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        }
    }, [isError, error, navigate]);

    if (isLoading) {
        return (
            <Loader size="medium" />
        );
    }

    if (isError) {
        return null;
    }

    return (
        <>
            {(!data || data.length === 0)
                ? (<div className="page-section">
                    <h4>Нет данных</h4>
                </div>)
                : (<div className="table-wrapper">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Проба</th>
                                <th>Тип металла</th>
                                <th>Метод</th>
                                <th>Дата</th>
                                <th>Статус</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data && data.map((r, i) => (
                                <tr key={i}>
                                    <td>{r.sampleId}</td>
                                    <td>{r.metalType}</td>
                                    <td>{r.analysisMethod}</td>
                                    <td>{new Date(r.testDate).toLocaleString()}</td>
                                    <td><span className={"badge badge-" + (r.status === 'APPROVED' ? "success" : "danger")}>{r.statusName}</span></td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>)}
        </>
    );
}