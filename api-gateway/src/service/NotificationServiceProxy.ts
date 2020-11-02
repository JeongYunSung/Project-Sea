import fetch from 'node-fetch'
import Options from '../service/Options';
import DataLoader from 'data-loader'

class NotificationServiceProxy {

    private notificationService: string;

    constructor(options: Options) {
        this.notificationService = `${options.baseUrl}/notifications`
    }
}

export default NotificationServiceProxy;