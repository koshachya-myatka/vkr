import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/general/Header";
import Footer from "../components/general/Footer";
import Loader from "../components/general/Loader";
import PaginationButton from "../components/general/PaginationButton";
import AdminSearchPanel from "../components/adminPage/AdminSearchPanel";
import AdminUsersGrid from "../components/adminPage/AdminUsersGrid";
import { getUsers } from "../api/api";

const PAGE_SIZE = 20;

export default function AdminPage() {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [offset, setOffset] = useState(0);
    const [data, setData] = useState([]);
    const [totalPages, setTotalPages] = useState(1);
    const [pageNumber, setPageNumber] = useState(1);

    const [filter, setFilter] = useState({
        username: null,
        name: null,
        surname: null,
        role: null
    });

    const loadData = async (customFilter = filter, customOffset = offset, customLimit = PAGE_SIZE) => {
        setLoading(true);
        try {
            const dto = {
                offset: customOffset,
                limit: customLimit,
                username: customFilter.username ?? null,
                name: customFilter.name ?? null,
                surname: customFilter.surname ?? null,
                role: customFilter.role ?? null
            };
            const response = await getUsers(dto);
            setData(response.data.items);
            setTotalPages(response.data.totalPages)
        } catch (error) {
            navigate("/error");
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

    const handleNext = () => {
        setOffset(prev => prev + PAGE_SIZE);
        setPageNumber((prev) => prev + 1);
    };

    const handlePrev = () => {
        setOffset(prev => Math.max(prev - PAGE_SIZE, 0));
        setPageNumber((prev) => Math.max(prev - 1, 1));
    };

    return (
        <>
            <title>Управление</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Пользователи</h1>
                </div>

                <div className="page-section">
                    <AdminSearchPanel onSearch={handleSearch} />
                </div>

                <div className="page-section">
                    {
                        loading
                            ? <Loader size="large" />
                            : <AdminUsersGrid data={data} />
                    }
                </div>

                <div className="pagination">
                    <PaginationButton
                        onClick={handlePrev}
                        disabled={offset === 0}
                    >
                        Назад
                    </PaginationButton>

                    <span className="badge badge-info">
                        {pageNumber}
                    </span>

                    <PaginationButton
                        onClick={handleNext}
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