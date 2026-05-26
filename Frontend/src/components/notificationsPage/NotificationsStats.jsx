import { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import Loader from "../general/Loader";
import { getNotificationsStats } from "../../api/api";

export default function NotificationsStats() {
    const navigate = useNavigate();

    const { data: stats, isLoading, isError, error } = useQuery({
        queryKey: ['notifications-stats'],
        queryFn: () => getNotificationsStats().then(res => res.data)
    });

    useEffect(() => {
        if (isError) {
            let errorMessage = error?.response?.data?.message || null;
            if (error.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        }
    }, [isError, error, navigate]);

    if (isError) {
        return null;
    }

    return (
        <div className="page-section">
            <h3>Статистика за сегодня</h3>
            {isLoading
                ? (<Loader size="small" />)
                : (<>
                    {(!stats) && (<h4>Нет данных</h4>)}
                    {stats &&
                        (
                            <div className="grid-cards">
                                <div className="card card-hover info-card">
                                    <div className="stat-block">
                                        <span className="stat-label">
                                            Всего уведомлений
                                        </span>
                                        <span className="stat-value badge badge-info">
                                            {stats.totalToday ?? 0}
                                        </span>
                                    </div>
                                </div>

                                <div className="card card-hover info-card">
                                    <div className="stat-block">
                                        <span className="stat-label">Создано</span>
                                        <span className="stat-value badge badge-danger">
                                            {stats.createdCount ?? 0}
                                        </span>
                                    </div>
                                </div>

                                <div className="card card-hover info-card">
                                    <div className="stat-block">
                                        <span className="stat-label">В работе</span>
                                        <span className="stat-value badge badge-warning">
                                            {stats.inProgressCount ?? 0}
                                        </span>
                                    </div>
                                </div>

                                <div className="card card-hover info-card">
                                    <div className="stat-block">
                                        <span className="stat-label">Ложное срабатывание</span>
                                        <span className="stat-value badge badge-info">
                                            {stats.falsePositiveCount ?? 0}
                                        </span>
                                    </div>
                                </div>

                                <div className="card card-hover info-card">
                                    <div className="stat-block">
                                        <span className="stat-label">Причина устранена</span>
                                        <span className="stat-value badge badge-success">
                                            {stats.resolvedCount ?? 0}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        )
                    }
                </>)
            }
        </div>
    );
}