import SimpleLink from "../general/SimpleLink";
import { useLocation } from 'react-router-dom';

export default function MetalBatchesTable({ data }) {
    if (!data || data.length === 0) {
        return (
            <div className="page-section">
                <h4>Нет данных</h4>
            </div>
        )
    };

    const location = useLocation();
    const url = location.pathname.split("/metals")[0];

    return (
        <div className="table-wrapper">
            <table className="table">
                <thead>
                    <tr>
                        <th>ID пробы</th>
                        <th>ID оборудования</th>
                        <th>Поступление</th>
                        <th>Окончание анализов</th>
                        <th>Статус</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {data && data.map((row) => (
                        <tr key={row.batchId}>
                            <td>{row.batchId}</td>
                            <td>{row.equipmentId}</td>
                            <td>{row.startTime}</td>
                            <td>{row.endTime}</td>
                            <td><span className="badge badge-info">{row.statusName}</span></td>
                            <td><SimpleLink link={url + "/batches/" + row.batchId} text="Подробнее" style={{ textDecoration: 'none' }} /></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}