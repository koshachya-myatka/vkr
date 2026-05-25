import { useEffect, useState } from "react";
import Header from "../general/Header";
import Footer from "../general/Footer";
import Loader from "../general/Loader";
import PaginationButton from "../general/PaginationButton";
import NotificationsSearchPanel from "./NotificationsSearchPanel";
import NotificationsGrid from "./NotificationsGrid";
import NotificationsStats from "./NotificationsStats";
import { getAllNotifications, getNotificationsStats } from "../../api/api";

const PAGE_SIZE = 20;

export default function NotificationsPage() {
    const role = localStorage.getItem("role");
    const isManager = role === "MANAGEMENT";

    const [loading, setLoading] = useState(false);
    const [offset, setOffset] = useState(0);
    const [pageNumber, setPageNumber] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [data, setData] = useState([]);
    const [stats, setStats] = useState(null);

    const [filter, setFilter] = useState({
        equipmentId: null,
        signalSource: null,
        status: null,
        dateFrom: null,
        dateTo: null
    });

    const loadData = async (customFilter = filter, customOffset = offset) => {
        setLoading(true);
        try {
            const response =
                await getAllNotifications({
                    offset: customOffset,
                    limit: PAGE_SIZE,
                    ...customFilter
                });
            setData(response.data.items);
            setTotalPages(response.data.totalPages);
            if (isManager) {
                const statsResponse = await getNotificationsStats();
                setStats(statsResponse.data);
            }
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
            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Уведомления о сбоях</h1>
                </div>

                {
                    isManager && stats && (
                        <div className="page-section">
                            <h3>Статистика за сегодня</h3>
                            <NotificationsStats stats={stats} />
                        </div>
                    )
                }

                <div className="page-section">
                    <NotificationsSearchPanel
                        onSearch={handleSearch}
                    />
                </div>

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