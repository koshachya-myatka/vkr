import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Loader from '../general/Loader';
import { getLastBatches } from "../../api/api";

export default function LastProductionRecordTable() {
  const navigate = useNavigate();

  const { data: data, isLoading, isError, error } = useQuery({
    queryKey: ['dashboard-prod-last-batches'],
    queryFn: () => getLastBatches().then(res => res.data)
  });

  if (isLoading) {
    return (
      <Loader size="medium" />
    );
  }

  if (isError) {
    navigate("/error");
  }

  if (!data.length) {
    <div className="page-section">
      <h4>Нет данных</h4>
    </div>
  }

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