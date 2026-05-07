export default function MetalStatisticsCard({
    metalType,
    metalTypeName,
    batchesCount,
    averageOutputYield,
    defectivePercent
}) {

    return (
        <div>
            <h3>{metalTypeName}</h3>            
            <div>
                <p>Обработано партий: {batchesCount}</p>
                <p>Ср. выход годного: {averageOutputYield}%</p>
                <p>Процент брака: {defectivePercent}%</p>
            </div>
        </div>
    );
}