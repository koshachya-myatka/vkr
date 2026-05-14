import { useEffect, useRef } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import Loader from "./Loader";
import NotificationItem from './NotificationItem';
import { getNotifications, markNotificationViewed } from '../../api/api';

export default function NotificationDropdown({ onClose }) {
    const ref = useRef();
    const queryClient = useQueryClient();

    const { data: items, isLoading } = useQuery({
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
        const oldLength = items.length;
        const updatedItems = items.filter(i => i.id !== id);
        queryClient.setQueryData(['notifications'], updatedItems);
        queryClient.setQueryData(['unviewedNotificationsCount'], updatedItems.length);
        try {
            await markNotificationViewed(id);
        } catch (e) {
            queryClient.invalidateQueries({ queryKey: ['notifications'] });
            queryClient.setQueryData(['unviewedNotificationsCount'], oldLength);
        }
    };

    const clearAll = async () => {
        const oldLength = items.length;
        queryClient.setQueryData(['notifications'], []);
        queryClient.setQueryData(['unviewedNotificationsCount'], 0);
        try {
            for (const item of items) {
                await markNotificationViewed(item.id);
            }
        } catch (e) {
            queryClient.invalidateQueries({ queryKey: ['notifications'] });
            queryClient.setQueryData(['unviewedNotificationsCount'], oldLength);
        }
    };

    return (
        <div ref={ref} className="notification-dropdown">
            <div className="notification-dropdown-header">
                <h3>Уведомления</h3>
                {
                    items && items.length > 0 && (
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
            {isLoading
                ? <Loader size="supersmall" />
                : (
                    <div className="notification-dropdown-list">
                        {
                            !items || items?.length === 0 && (
                                <div className="text-secondary">
                                    Нет уведомлений
                                </div>
                            )
                        }
                        {
                            items && items.map(item => (
                                <NotificationItem
                                    key={item.id}
                                    item={item}
                                    onDelete={remove}
                                />
                            ))
                        }
                    </div>
                )}
        </div>
    );
}