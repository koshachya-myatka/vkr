import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Scatter } from 'recharts';

const STATUS_COLORS = {
    NORMAL: '#22c55e',
    WARNING: '#eab308',
    ALARM: '#ef4444'
};

export default function ScadaParameterGraph({ parameter }) {
    const data = parameter.values.map(item => ({
        ...item,
        timestamp: new Date(item.time).toLocaleTimeString(),
        color: STATUS_COLORS[item.status]
    }));

    const RenderCustomDot = (props) => {
        const { cx, cy, payload } = props;
        if (payload.color === "#22c55e") {
            return null;
        }
        return (<circle cx={cx} cy={cy} r={5} fill={payload.color} stroke="#fff" strokeWidth={1} />);
    };

    return (
        <div className="flex-column gap-md">
            <div className="flex-between">
                <h3>
                    {parameter.parameter}, {parameter.unit}
                </h3>
                <span className="badge badge-secondary">
                    {parameter.equipmentId}
                </span>
            </div>
            <div style={{ width: '100%', height: 300, minWidth: 0 }}>
                <ResponsiveContainer width="100%" height="100%" initialDimension={{ width: '100%', height: 300 }}>
                    <LineChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis
                            dataKey="timestamp"
                            tick={{ fontSize: 12 }}
                        />
                        <YAxis
                            dataKey="value"
                            tick={{ fontSize: 12 }}
                        />
                        <Tooltip />
                        <Line
                            type="monotone"
                            dataKey="value"
                            stroke="#2563eb"
                            strokeWidth={2}
                            dot={<RenderCustomDot />}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
}