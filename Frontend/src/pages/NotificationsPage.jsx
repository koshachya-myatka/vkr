import { useEffect, useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import Header from "../components/general/Header";
import HeaderIconButton from "../components/general/HeaderIconButton";
import Footer from "../components/general/Footer";
import Loader from "../components/general/Loader";
import PaginationButton from "../components/general/PaginationButton";
import NotificationsSearchPanel from "../components/notificationsPage/NotificationsSearchPanel";
import NotificationsGrid from "../components/notificationsPage/NotificationsGrid";
import NotificationsStats from "../components/notificationsPage/NotificationsStats";
import { getAllNotifications } from "../api/api";

const PAGE_SIZE = 20;

export default function NotificationsPage() {
    const queryClient = useQueryClient();
    const [hasNewNotifications, setHasNewNotifications] = useState(false);

    const role = localStorage.getItem("role");
    const isManager = role === "MANAGEMENT";

    const [loading, setLoading] = useState(false);
    const [offset, setOffset] = useState(0);
    const [pageNumber, setPageNumber] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [data, setData] = useState([]);

    const [filter, setFilter] = useState({
        equipmentId: null,
        signalSource: null,
        status: null,
        dateFrom: null,
        dateTo: null
    });

    useEffect(() => {
        const handler = () => {
            setHasNewNotifications(true);
        };
        window.addEventListener("notifications-new", handler);
        return () => {
            window.removeEventListener("notificatios-new", handler);
        };

    }, []);

    const refreshNotifications = async () => {
        await loadData();
    };

    const loadData = async (customFilter = filter, customOffset = offset) => {
        setLoading(true);
        setHasNewNotifications(false);
        try {
            const response =
                await getAllNotifications({
                    offset: customOffset,
                    limit: PAGE_SIZE,
                    ...customFilter
                });
            setData(response.data.items);
            setTotalPages(response.data.totalPages);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, [offset]);

    const handleSearch = (newFilter) => {
        setOffset(0);
        setPageNumber(1);
        setFilter(newFilter);
        loadData(newFilter, 0);
    };

    return (
        <>
            <title>Сбои</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Уведомления о сбоях</h1>
                </div>

                {isManager && (<NotificationsStats />)}

                <div className="page-section">
                    <NotificationsSearchPanel
                        onSearch={handleSearch}
                    />
                </div>

                {
                    hasNewNotifications && (
                        <div className="notification-card">
                            <p>Появились новые уведомления</p>
                            <HeaderIconButton
                                icon="refresh"
                                onClick={refreshNotifications}
                            />
                        </div>
                    )
                }

                <div className="page-section">
                    {
                        loading
                            ? <Loader size="large" />
                            : (
                                <NotificationsGrid
                                    data={data}
                                    reload={loadData}
                                />
                            )
                    }
                </div>

                <div className="pagination">
                    <PaginationButton
                        onClick={() => {
                            setOffset(prev => Math.max(prev - PAGE_SIZE, 0));
                            setPageNumber(prev => Math.max(prev - 1, 1));
                        }}
                        disabled={offset === 0}
                    >
                        Назад
                    </PaginationButton>

                    <span className="badge badge-info">
                        {pageNumber}
                    </span>

                    <PaginationButton
                        onClick={() => {
                            setOffset(prev => prev + PAGE_SIZE);
                            setPageNumber(prev => prev + 1);
                        }}
                        disabled={pageNumber >= totalPages}
                    >
                        Вперёд
                    </PaginationButton>
                </div>
            </main>

            <Footer />
        </>
    );
}