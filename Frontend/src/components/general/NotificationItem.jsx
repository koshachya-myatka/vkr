export default function NotificationItem({ item, onDelete }) {
    const color = {
        WARNING: 'orange',
        ALARM: 'red'
    }[item.severity];

    return (
        <div style={{ background: color, padding: '10px', marginBottom: '5px' }}>
            <table>
                <tbody>
                    <tr>
                        <td>{item.createdAt}</td>
                        <td>{item.signalSource}</td>
                        <td>{item.message}</td>
                        <td>{item.equipmentId}</td>
                        <td><button onClick={() => onDelete(item.id)}>X</button></td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
}