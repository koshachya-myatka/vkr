import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import HeaderIconButton from './HeaderIconButton';
import HeaderLinkButton from './HeaderLinkButton';
import NotificationDropdown from './NotificationDropdown';
import { getActiveNotifications } from '../../api/api';

export default function Header() {
    const location = useLocation();
    const navigate = useNavigate();

    const [notificationsOpen, setNotificationsOpen] = useState(false);

    const { data: unviewedCount = 0 } = useQuery({
        queryKey: ['unviewedNotificationsCount'],
        queryFn: async () => {
            const { data } = await getActiveNotifications();
            return data.length;
        },
        staleTime: 0
    });

    const role = localStorage.getItem('role');
    const isAdmin = role === "ADMIN";
    const isManager = role === "MANAGEMENT";
    const isProd = role === "PRODUCTION";
    const isLab = role === "LABORATORY";

    const logout = () => {
        localStorage.clear();
        window.location.href = "/login";
    };

    return (
        <header className="header">
            <div className="header-top">
                <div className="header-logo">
                    <span className="material-symbols-outlined">
                        assessment
                    </span>
                    Витрина качества металлов
                </div>

                <div className="header-actions">
                    {(isManager || isProd) && (
                        <div className="notification-wrapper">
                            <HeaderIconButton
                                icon="notifications"
                                hasBadge={unviewedCount > 0}
                                badgeNumber={unviewedCount}
                                onClick={() =>
                                    setNotificationsOpen(prev => !prev)
                                }
                            />
                            {
                                notificationsOpen && (
                                    <NotificationDropdown
                                        onClose={() =>
                                            setNotificationsOpen(false)
                                        }
                                    />
                                )
                            }
                        </div>
                    )}

                    <HeaderIconButton
                        icon="person"
                        onClick={() => navigate("/profile")}
                    />

                    <HeaderIconButton
                        icon="logout"
                        danger
                        onClick={logout}
                    />
                </div>
            </div>
            <nav className="header-nav">
                {(isAdmin || isManager || isLab)
                    && <HeaderLinkButton text="Лаборатория" link="/laboratory" active={location.pathname.startsWith('/laboratory')} />}
                {(isAdmin || isManager || isProd)
                    && <HeaderLinkButton text="Производство" link="/production" active={location.pathname.startsWith('/production')} />}
                {(isAdmin || isManager)
                    && <HeaderLinkButton text="Руководство" link="/management" active={location.pathname.startsWith('/management')} />}
                {isAdmin
                    && <HeaderLinkButton text="Управление" link="/admin" active={location.pathname.startsWith('/admin')} />}
            </nav>
        </header>
    );
}