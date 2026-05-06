import { useEffect } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { connectWebSocket, disconnectWebSocket } from './websocket';

export const WebSocketProvider = ({ children }) => {
    const queryClient = useQueryClient();

    useEffect(() => {
        connectWebSocket((type) => {
            switch (type) {
                case 'notifications':
                    queryClient.invalidateQueries({ queryKey: ['notifications'] });
                    break;
                case 'mes':
                    queryClient.invalidateQueries({ queryKey: ['batch-page-batchData'] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-prod-last-batches'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-mes'] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-metal-grid'] });
                    break;
                case 'lims':
                    queryClient.invalidateQueries({ queryKey: ['batch-page-prod-lims'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-lab-lims'] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-lab-last-lims'] });
                    break;
                case 'scada':
                    // queryClient.invalidateQueries({ queryKey: [''] });
                    break;
            }
        });

        return () => disconnectWebSocket();
    }, [queryClient]);

    return children;
};