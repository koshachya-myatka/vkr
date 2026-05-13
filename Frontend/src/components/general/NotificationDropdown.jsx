import { useEffect, useRef } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import NotificationItem from './NotificationItem';
import { getNotifications, markNotificationViewed } from '../../api/api';

export default function NotificationDropdown({ onClose }) {
    const ref = useRef();
    const queryClient = useQueryClient();

    const { data: items = [] } = useQuery({
        queryKey: ['notifications'],
        queryFn: () => getNotifications().then(res => res.data)
    });

    useEffect(() => {
        const handler = (e) => {
            if (ref.current && !ref.current.contains(e.target)) {
                onClose();
            }
        };
        document.addEventListener("mousedown", handler);
        return () =>
            document.removeEventListener(
                "mousedown",
                handler
            );
    }, []);

    const remove = async (id) => {
        queryClient.setQueryData(
            ['notifications'],
            (old = []) => old.filter(i => i.id !== id)
        );
        try {
            await markNotificationViewed(id);
        } catch (e) {
            queryClient.invalidateQueries({ queryKey: ['notifications'] });
        }
    };

    const clearAll = async () => {
        queryClient.setQueryData(
            ['notifications'],
            []
        );
        try {
            for (const item of items) {
                await markNotificationViewed(item.id);
            }
        } catch (e) {
            queryClient.invalidateQueries({ queryKey: ['notifications'] });
        }
    };

    return (
        <div ref={ref} className="notification-dropdown">
            <div className="notification-dropdown-header">
                <h3>Уведомления</h3>
                {
                    items.length > 0 && (
                        <button
                            className="btn btn-danger"
                            onClick={clearAll}
                        >
                            Очистить
                        </button>
                    )
                }
            </div>

            <div className="divider" />

            <div className="notification-dropdown-list">
                {
                    items.length === 0 && (
                        <div className="text-secondary">
                            Нет уведомлений
                        </div>
                    )
                }
                {
                    items.map(item => (
                        <NotificationItem
                            key={item.id}
                            item={item}
                            onDelete={remove}
                        />
                    ))
                }
            </div>
        </div>
    );
}