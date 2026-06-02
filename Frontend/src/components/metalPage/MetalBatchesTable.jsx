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
                        <th>ID партии</th>
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
                            <td>{new Date(row.startTime).toLocaleString()}</td>
                            <td>{row.endTime ? new Date(row.endTime).toLocaleString() : ''}</td>
                            <td>
                                <span className={"badge badge-" + (row.processStatus === 'ACCEPTED' ? "success"
                                    : (row.processStatus === "DEFECTIVE" ? "danger" : "info"))}>
                                    {row.statusName}
                                </span>
                            </td>
                            <td>
                                <SimpleLink
                                    className="btn btn-primary"
                                    link={url + "/batches/" + row.batchId}
                                    text="Подробнее"
                                    style={{ textDecoration: 'none', padding: "6px" }} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}