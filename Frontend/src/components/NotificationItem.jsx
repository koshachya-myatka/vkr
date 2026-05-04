export default function NotificationItem({ item, onDelete }) {
    const color = {
        WARNING: 'orange',
        ALARM: 'red'
    }[item.severity];

    return (
        <div style={{ background: color, padding: '10px', marginBottom: '5px' }}>
            {item.message}
            <button onClick={() => onDelete(item.id)}>X</button>
        </div>
    );
}