import { useQuery, useQueryClient } from '@tanstack/react-query';
import { getNotifications, markNotificationViewed } from '../../api/api';
import NotificationItem from './NotificationItem';

export default function NotificationPanel() {
    const queryClient = useQueryClient();

    const { data: items = [] } = useQuery({
        queryKey: ['notifications'],
        queryFn: () => getNotifications().then(res => res.data),
    });

    const remove = async (id) => {
        queryClient.setQueryData(['notifications'], (old = []) =>
            old.filter(i => i.id !== id)
        );
        try {
            await markNotificationViewed(id);
        } catch (e) {
            queryClient.invalidateQueries({ queryKey: ['notifications'] });
        }
    };

    return (
        <div className="page-section">
            {items.map(i => (
                <NotificationItem key={i.id} item={i} onDelete={remove} />
            ))}
        </div>
    );
}