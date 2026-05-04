import { useEffect, useState } from 'react';
import { getNotifications, markNotificationViewed } from '../api/api';
import NotificationItem from './NotificationItem';

export default function NotificationPanel() {
    const [items, setItems] = useState([]);

    useEffect(() => {
        load();
    }, []);

    const load = async () => {
        const res = await getNotifications();
        setItems(res.data);
    };

    const remove = async (id) => {
        await markNotificationViewed(id);
        setItems(prev => prev.filter(i => i.id !== id));
    };

    return (
        <div>
            {items.map(i => (
                <NotificationItem key={i.id} item={i} onDelete={remove} />
            ))}
        </div>
    );
}