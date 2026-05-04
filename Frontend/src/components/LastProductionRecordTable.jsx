import { useEffect, useState } from "react";
import { getLastBatches } from "../api/api";

export default function LastProductionRecordTable() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    getLastBatches()
      .then((res) => {
        setData(res.data);
        setLoading(false);
      })
      .catch(() => {
        setError(true);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div style={{ padding: '10px' }}>
        <div style={{
          height: '200px',
          width: '100%',
          background: 'linear-gradient(180deg, #e0e0e0 25%, #f5f5f5 50%, #e0e0e0 75%)',
          backgroundSize: '100% 200%',
          animation: 'loading 1.5s infinite'
        }} />

        <style>
          {`
            @keyframes loading {
                0% { background-position: 0% 0%; }
                100% { background-position: 0% 100%; }
            }
          `}
        </style>
      </div>
    );
  }

  if (error || !data.length) {
    return <div>Данные не найдены</div>;
  }

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
            <td>{b.batch_id}</td>
            <td>{b.metal_type}</td>
            <td>{b.start_time}</td>
            <td>{b.end_time}</td>
            <td>{b.status_name}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}