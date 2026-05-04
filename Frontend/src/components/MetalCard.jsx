export default function MetalCard({ metal }) {
    if (!metal) return null;

    return (
        <div style={{
            border: '1px solid gray',
            padding: 10,
            borderRadius: 8,
            background: '#fff'
        }}>
            <img
                src={`/metals/${metal.metalType}.png`}
                width={80}
                alt={metal.metalType}
                onError={(e) => {
                    e.target.style.display = 'none';
                }}
            />

            <h3>{metal.metalType}</h3>

            <p>Total: {metal.total ?? 0}</p>
            <p>Arrival: {metal.arrival ?? 0}</p>
            <p>Processing: {metal.processing ?? 0}</p>
            <p>Analysis: {metal.analysis ?? 0}</p>
            <p>Accepted: {metal.accepted ?? 0}</p>
            <p>Defective: {metal.defective ?? 0}</p>
        </div>
    );
}