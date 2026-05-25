import { useEffect, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import Loader from "./Loader";
import NotificationItem from './NotificationItem';
import { getActiveNotifications, markNotificationInProgress } from '../../api/api';

export default function NotificationDropdown({ onClose }) {
    const ref = useRef();
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const { data: items, isLoading } = useQuery({
        queryKey: ['notifications'],
        queryFn: () => getActiveNotifications().then(res => res.data)
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

    const takeToWork = async (id) => {
        const oldLength = items.length;
        const updatedItems = items.filter(i => i.id !== id);
        queryClient.setQueryData(['notifications'], updatedItems);
        queryClient.setQueryData(['unviewedNotificationsCount'], updatedItems.length);
        try {
            await markNotificationInProgress(id);
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
                await markNotificationInProgress(item.id);
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
                            Все на проверку
                        </button>
                    )
                }
            </div> 

            <Link to="/notifications" className="link">
                Все уведомления о сбоях
            </Link>
            
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
                                    onTake={takeToWork}
                                />
                            ))
                        }
                    </div>
                )}
        </div>
    );
}