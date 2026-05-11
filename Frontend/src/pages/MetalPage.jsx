import { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import Header from "../components/general/Header";
import Footer from "../components/general/Footer";
import SearchPanel from "../components/metalPage/SearchPanel";
import MetalBatchesTable from "../components/metalPage/MetalBatchesTable";
import PaginationButton from "../components/general/PaginationButton";
import { getMetalBatches } from "../api/api";
import Loader from "../components/general/Loader";

const PAGE_SIZE = 20;

export default function MetalPage() {
    const navigate = useNavigate();
    const { id } = useParams(); // metalType
    const [data, setData] = useState([]);
    const [offset, setOffset] = useState(0);
    const [filter, setFilter] = useState({});
    const [loading, setLoading] = useState(false);
    const [pageNumber, setPageNumber] = useState(1);

    const loadData = async (customFilter = filter, customOffset = offset) => {
        setLoading(true);
        try {
            const dto = {
                metalType: id,
                batchId: customFilter.batchId ?? null,
                equipmentId: customFilter.equipmentId ?? null,
                startTime: customFilter.startTime ?? null,
                endTime: customFilter.endTime ?? null,
                processStatus: customFilter.processStatus ?? null,
                offset: customOffset,
            };
            const res = await getMetalBatches(dto);
            setData(res.data);
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
        setFilter(newFilter);
        loadData(newFilter, 0);
    };

    const handleNext = () => {
        setOffset((prev) => prev + PAGE_SIZE);
        setPageNumber(offset % PAGE_SIZE + 1);
    };

    const handlePrev = () => {
        setOffset((prev) => Math.max(prev - PAGE_SIZE, 0));
        setPageNumber(offset % PAGE_SIZE + 1);
    };

    return (
        <>
            <title>Металл - {id}</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Металл {id[0] + id[1].toLowerCase()}</h1>
                </div>

                <div className="page-section">
                    <SearchPanel onSearch={handleSearch} />
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
                        disabled={data.length < PAGE_SIZE}
                    >
                        Вперёд
                    </PaginationButton>
                </div>
            </main>

            <Footer />
        </>
    );
}