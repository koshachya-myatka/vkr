import { useQuery } from '@tanstack/react-query';
import { getLastBatches } from "../api/api";

export default function LastProductionRecordTable() {
  const { data: data, isLoading, isError, error } = useQuery({
    queryKey: ['dashboard-prod-last-batches'],
    queryFn: () => getLastBatches().then(res => res.data)
  });

  if (isLoading) return <div>Загрузка...</div>;
  if (isError || !data.length) return <div>Ошибка: {error.message}</div>;

  return (
    <table border="1" width="100%">
      <thead>
        <tr>
          <th>Batch</th>
          <th>Metal</th>
          <th>Start</th>
          <th>End</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {data.map((b, i) => (
          <tr key={i}>
            <td>{b.batchId}</td>
            <td>{b.metalType}</td>
            <td>{b.startTime}</td>
            <td>{b.endTime}</td>
            <td>{b.statusName}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}