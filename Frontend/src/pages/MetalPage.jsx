import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import Header from "../components/Header";
import SearchPanel from "../components/SearchPanel";
import MetalBatchesTable from "../components/MetalBatchesTable";
import PaginationButton from "../components/PaginationButton";
import { getMetalBatches } from "../api/api";

const PAGE_SIZE = 20;

export default function MetalPage() {
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
                startTime: customFilter.startTime ?? null,
                endTime: customFilter.endTime ?? null,
                processStatus: customFilter.processStatus ?? null,
                offset: customOffset,
            };
            const res = await getMetalBatches(dto);
            setData(res.data);
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
        <div>
            <title>Металл - {id}</title>

            <Header />
            
            <h2>Metal: {id}</h2>

            <SearchPanel onSearch={handleSearch} />

            {loading ? (
                <div>Loading...</div>
            ) : (
                <MetalBatchesTable data={data} />
            )}

            <div style={{ marginTop: "20px" }}>
                <PaginationButton
                    onClick={handlePrev}
                    disabled={offset === 0}
                >
                    Prev
                </PaginationButton>
                <p>{pageNumber}</p>
                <PaginationButton
                    onClick={handleNext}
                    disabled={data.length < PAGE_SIZE}
                >
                    Next
                </PaginationButton>
            </div>
        </div>
    );
}