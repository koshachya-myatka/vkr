import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Loader from '../general/Loader';
import SimpleLink from '../general/SimpleLink';
import { getLastBatches } from "../../api/api";

export default function LastProductionRecordTable() {
  const navigate = useNavigate();
  const url = location.pathname;

  const { data: data, isLoading, isError, error } = useQuery({
    queryKey: ['dashboard-prod-last-batches'],
    queryFn: () => getLastBatches().then(res => res.data)
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
                <th>Партия</th>
                <th>Тип металла</th>
                <th>ID оборудования</th>
                <th>Поступление</th>
                <th>Окончание анализов</th>
                <th>Статус</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {data && data.map((b, i) => (
                <tr key={i}>
                  <td>{b.batchId}</td>
                  <td>{b.metalType}</td>
                  <td>{b.equipmentId}</td>
                  <td>{new Date(b.startTime).toLocaleString()}</td>
                  <td>{b.endTime ? new Date(b.endTime).toLocaleString() : ""}</td>
                  <td>
                    <span className={"badge badge-" + (b.processStatus === 'ACCEPTED' ? "success"
                      : (b.processStatus === "DEFECTIVE" ? "danger" : "info"))}>{b.statusName}</span>
                  </td>
                  <td>
                    <SimpleLink
                      className="btn btn-primary"
                      link={url + "/batches/" + b.batchId}
                      text="Подробнее"
                      style={{ textDecoration: 'none', padding: "6px" }} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>)}
    </>
  );
}