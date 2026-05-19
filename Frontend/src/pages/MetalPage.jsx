import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import Header from "../components/general/Header";
import Footer from "../components/general/Footer";
import MetalSearchPanel from "../components/metalPage/MetalSearchPanel";
import MetalBatchesTable from "../components/metalPage/MetalBatchesTable";
import PaginationButton from "../components/general/PaginationButton";
import { getMetalBatches } from "../api/api";
import Loader from "../components/general/Loader";

const PAGE_SIZE = 20;

export default function MetalPage() {
    const navigate = useNavigate();
    const { id: metalType } = useParams();
    const [offset, setOffset] = useState(0);
    const [data, setData] = useState([]);
    const [filter, setFilter] = useState({});
    const [loading, setLoading] = useState(false);
    const [totalPages, setTotalPages] = useState(1);
    const [pageNumber, setPageNumber] = useState(1);

    const loadData = async (customFilter = filter, customOffset = offset, customLimit = PAGE_SIZE) => {
        setLoading(true);
        try {
            const dto = {
                offset: customOffset,
                limit: customLimit,
                metalType: metalType,
                batchId: customFilter.batchId ?? null,
                equipmentId: customFilter.equipmentId ?? null,
                startTime: customFilter.startTime ?? null,
                endTime: customFilter.endTime ?? null,
                processStatus: customFilter.processStatus ?? null,               
            };
            const res = await getMetalBatches(dto);
            setData(res.data.items);
            setTotalPages(res.data.totalPages);
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
        setOffset((prev) => prev + PAGE_SIZE);
        setPageNumber((prev) => prev + 1);
    };

    const handlePrev = () => {
        setOffset((prev) => Math.max(prev - PAGE_SIZE, 0));
        setPageNumber((prev) => Math.max(prev - 1, 1));
    };

    return (
        <>
            <title>Металл - {metalType}</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Металл {metalType[0] + metalType[1].toLowerCase()}</h1>
                </div>

                <div className="page-section">
                    <MetalSearchPanel onSearch={handleSearch} />
                </div>

                <div className="page-section">
                    {loading ? (
                        <Loader size="large" />
                    ) : (
                        <MetalBatchesTable data={data} />
                    )}
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