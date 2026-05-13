import { useQuery } from "@tanstack/react-query";
import { useNavigate } from 'react-router-dom';
import MetalStatisticsCard from "./MetalStatisticsCard";
import Loader from "../general/Loader";
import { getMetalStatisticsCards } from "../../api/api";

export default function LastMetalStatisticsGrid() {
    const navigate = useNavigate();

    const { data: data, isLoading, isError, error } = useQuery({
        queryKey: ["dashboard-manag-last-statistics"],
        queryFn: () => getMetalStatisticsCards().then(res => res.data)
    });

    if (isLoading) {
        return (
            <Loader size="medium" />
        );
    }

    if (isError) {
        navigate("/error");
    }

    return (
        <>
            {(!data || data.length === 0)
                ? (<div className="page-section">
                    <h4>Нет данных</h4>
                </div>)
                : (<div className="grid-cards">
                    {data?.map((item) => (
                        <MetalStatisticsCard
                            key={item.metalType}
                            metalType={item.metalType}
                            metalTypeName={item.metalTypeName}
                            batchesCount={item.batchesCount}
                            averageOutputYield={item.averageOutputYield}
                            defectivePercent={item.defectivePercent}
                        />
                    ))}
                </div>)}
        </>
    );
}