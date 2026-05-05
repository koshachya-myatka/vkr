export default function MetalBatchesTable({ data }) {
    if (!data || data.length === 0) {
        return <div>No data found</div>;
    }

    return (
        <table border="1" cellPadding="10" width="100%">
            <thead>
                <tr>
                    <th>Batch ID</th>
                    <th>Start Time</th>
                    <th>End Time</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                {data.map((row) => (
                    <tr key={row.batchId}>
                        <td>{row.batchId}</td>
                        <td>{row.startTime}</td>
                        <td>{row.endTime}</td>
                        <td>{row.statusName}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}