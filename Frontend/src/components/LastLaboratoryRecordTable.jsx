import { useQuery } from '@tanstack/react-query';
import { getLastLims } from '../api/api';

export default function LastLaboratoryRecordTable() {
    const { data: data, isLoading, isError, error } = useQuery({
        queryKey: ['dashboard-lab-last-lims'],
        queryFn: () => getLastLims().then(res => res.data)
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError || !data.length) return <div>Ошибка: {error?.message}</div>;

    return (
        <table border="1" width="100%">
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
                        <td>{r.statusName}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}