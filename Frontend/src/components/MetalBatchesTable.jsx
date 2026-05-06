import SimpleLink from "./SimpleLink";
import { useLocation } from 'react-router-dom';

export default function MetalBatchesTable({ data }) {
    if (!data || data.length === 0) {
        return <div>No data found</div>;
    }
    const location = useLocation();
    const url = location.pathname.split("/metals")[0];

    return (
        <table border="1" cellPadding="10" width="100%">
            <thead>
                <tr>
                    <th>ID пробы</th>
                    <th>Поступление</th>
                    <th>Окончание анализов</th>
                    <th>Статус</th>
                </tr>
            </thead>
            <tbody>
                {data && data.map((row) => (
                    <tr key={row.batchId}>
                        <td>{row.batchId}</td>
                        <td>{row.startTime}</td>
                        <td>{row.endTime}</td>
                        <td>{row.statusName}</td>
                        <td><SimpleLink link={url + "/batches/" + row.batchId} text="Подробнее"/></td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}