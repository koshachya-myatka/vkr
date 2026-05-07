import { useQuery } from "@tanstack/react-query";
import MetalStatisticsCard from "./MetalStatisticsCard";
import { getMetalStatisticsCards } from "../api/api";

export default function LastMetalStatisticsGrid() {
    const { data: data, isLoading, isError, error } = useQuery({
        queryKey: ["dashboard-manag-last-statistics"],
        queryFn: () => getMetalStatisticsCards().then(res => res.data)
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError || !data.length) return <div>Ошибка: {error?.message}</div>;

    return (
        <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(4, 1fr)',
            gap: '10px'
        }}>
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
        </div>
    );
}