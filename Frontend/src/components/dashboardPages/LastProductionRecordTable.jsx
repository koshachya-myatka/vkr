import { useQuery } from '@tanstack/react-query';
import { getLastBatches } from "../../api/api";

export default function LastProductionRecordTable() {
  const { data: data, isLoading, isError, error } = useQuery({
    queryKey: ['dashboard-prod-last-batches'],
    queryFn: () => getLastBatches().then(res => res.data)
  });

  if (isLoading) return <div>Загрузка...</div>;
  if (isError || !data.length) return <div>Ошибка: {error?.message}</div>;

  return (
    <div className="table-wrapper">
      <table className="table">
        <thead>
          <tr>
            <th>Партия</th>
            <th>Тип металла</th>
            <th>Поступление</th>
            <th>Окончание анализов</th>
            <th>Статус</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map((b, i) => (
            <tr key={i}>
              <td>{b.batchId}</td>
              <td>{b.metalType}</td>
              <td>{b.startTime}</td>
              <td>{b.endTime}</td>
              <td><span className="badge badge-info">{b.statusName}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}